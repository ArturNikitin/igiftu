package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.Content
import com.svetka.igiftu.dto.ImageDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.entity.enums.Access
import com.svetka.igiftu.repository.WishRepository
import com.svetka.igiftu.service.entity.ImageService
import com.svetka.igiftu.service.entity.WishService
import java.nio.file.Files
import java.nio.file.Paths
import ma.glasnost.orika.MapperFacade
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.persistence.EntityNotFoundException
import mu.KotlinLogging


@Service
@Primary
class WishServiceImpl(
	private val wishRepository: WishRepository,
	private val mapper: MapperFacade,
	private val imageService: ImageService
) : WishService {


	companion object {
		private val logger = KotlinLogging.logger {  }
		private fun getDefaultPicture() : ByteArray {
			logger.info { "Reading wish image" }
			return Files.readAllBytes(Paths.get("src/main/resources/static/pictures/wish.jpeg"))
		}
	}

	private val format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

	@Transactional
	override fun get(ownerId: Long, ownerType: OwnerType): List<Content> = when (ownerType) {
		OwnerType.USER -> wishRepository.getAllByUserId(ownerId).map { mapWishToDto(it) }
		OwnerType.BOARD -> TODO()
	}

	@Transactional
	override fun delete(contentId: Long) = wishRepository.deleteById(contentId)

	override fun prepare(content: Content): Content {
		return prepare(content as WishDto)
	}

	@Transactional
	override fun update(contentId: Long, content: Content): Content = updateWish(contentId, content as WishDto)

	@Transactional
	override fun isOwner(ownerId: Long, contentId: Long?): Boolean {
		contentId ?: return false
		val wish = wishRepository.findById(contentId).orElseThrow { EntityNotFoundException("EntityNotFound") }
		return wish.user?.id == ownerId
	}

	private fun updateWish(contentId: Long, content: WishDto): WishDto {
		val wish =
			wishRepository.findById(contentId)
				.orElseThrow { EntityNotFoundException("Wish {$contentId} does not exists") }
				.also {
					it.access = Access.valueOf(content.access)
					it.name = content.name
					it.price = content.price
					it.lastModifiedDate = LocalDateTime.now()
					it.isAnalogPossible = content.isAnalogPossible
					it.isBooked = content.isBooked
					it.isCompleted = content.isCompleted
				}
		return mapWishToDto(wishRepository.save(wish))
	}

	private fun prepare(wishDto: WishDto): WishDto {
		val createdDate = LocalDateTime.now().format(format)
		val imageDto = dealWithImage(wishDto.image)
		return WishDto(
			name = wishDto.name,
			price = wishDto.price,
			access = wishDto.access,
			createdDate = createdDate,
			lastModifiedDate = createdDate,
			image = imageDto
		)
	}

	private fun dealWithImage(imageDto: ImageDto?): ImageDto {
		imageDto?.content ?: return imageService.getImage("default-wish")
		return imageService.saveImage(getUUIDNoDashes(), imageDto.content.toByteArray())
	}

	private fun getUUIDNoDashes() = UUID.randomUUID().toString().replace("-", "")

	private fun mapWishToDto(wish: Wish) = mapper.map(wish, WishDto::class.java)
}