package com.svetka.igiftu.service.entity.impl

import com.svetka.igiftu.dto.Content
import com.svetka.igiftu.dto.ImageDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.entity.enums.Access
import com.svetka.igiftu.repository.WishRepository
import com.svetka.igiftu.service.entity.ImageService
import com.svetka.igiftu.service.entity.WishService
import com.svetka.igiftu.service.manager.impl.OwnerType
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.persistence.EntityNotFoundException
import ma.glasnost.orika.MapperFacade
import mu.KotlinLogging
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Primary
class WishServiceImpl(
	private val wishRepository: WishRepository,
	private val mapper: MapperFacade,
	private val imageService: ImageService
) : WishService {


	companion object {
		private val logger = KotlinLogging.logger { }
		private fun getDefaultPicture(): ByteArray {
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

	override fun getWishes(ids: Iterable<Long>): Iterable<WishDto> =
		ids.flatMap(::getWish)
			.map { mapper.map(it, WishDto::class.java) }
			.toSet()

	private fun getWish(id: Long): Sequence<Wish> {
		val wish = wishRepository.findById(id)
		return if (wish.isPresent)
			sequenceOf(wish.get())
		else {
			logger.warn { "Wish with id $id not found" }
			emptySequence()
		}
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
		imageDto?.content ?: return imageService.getDefaultImage("default-wish")
		return imageService.saveImage(imageDto.content!!)
	}

	private fun getUUIDNoDashes() = UUID.randomUUID().toString().replace("-", "")

	private fun mapWishToDto(wish: Wish) = mapper.map(wish, WishDto::class.java)
}