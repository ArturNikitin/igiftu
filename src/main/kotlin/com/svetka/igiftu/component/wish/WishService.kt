package com.svetka.igiftu.component.wish

import com.svetka.igiftu.component.board.UserInfo
import com.svetka.igiftu.component.user.UserComponent
import com.svetka.igiftu.dateTimeFormat
import com.svetka.igiftu.dto.ImageDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.entity.enums.Access
import com.svetka.igiftu.repository.WishRepository
import com.svetka.igiftu.service.entity.ImageService
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException
import ma.glasnost.orika.MapperFacade
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class WishService(
	private val wishRepository: WishRepository,
	private val userService: UserComponent,
	private val mapper: MapperFacade,
	private val imageService: ImageService
) : WishComponent {

	private val logger = KotlinLogging.logger { }

	override fun getWishById(id: Long): WishDto {
		TODO("Not yet implemented")
	}

	override fun getWishesById(ids: Set<Long>): Iterable<WishDto> {
		logger.debug { "Getting wishes {$ids}" }
		val wishes = wishRepository.findAllById(ids).map(::getWishDto)
		logger.debug { "Got wishes $wishes" }
		return wishes
	}

	//	TODO deal with image
	override fun createWish(user: UserInfo, requestWish: WishDto): WishDto {
		val createdDate = LocalDateTime.now().format(dateTimeFormat)
		val wish = requestWish.apply {
			this.createdDate = createdDate
			lastModifiedDate = createdDate
		}.let { mapper.map(it, Wish::class.java) }

		return userService.addWishes(user.id, setOf(wish))
			.filter { it.lastModifiedDate == createdDate }
			.map { mapper.map(it, WishDto::class.java) }
			.first()
	}

	//	todo deal with image
	@Transactional
	override fun updateWish(user: UserInfo, requestWish: WishDto): WishDto {
		val updatedWish = findWish(requestWish.id!!)
			.also {
				it.access = Access.valueOf(requestWish.access)
				it.name = requestWish.name
				it.price = requestWish.price
				it.lastModifiedDate = LocalDateTime.now()
				it.isAnalogPossible = requestWish.isAnalogPossible
				it.isBooked = requestWish.isBooked
				it.isCompleted = requestWish.isCompleted
			}.let { wishRepository.save(it) }

		return getWishDto(updatedWish)
	}

	@Transactional
	override fun deleteWish(user: UserInfo, wishId: Long) =
		wishRepository.deleteById(wishId)


	private fun findWish(wishId: Long) =
		wishRepository.findById(wishId)
			.orElseThrow { EntityNotFoundException("Wish [$wishId] not found") }

	private fun dealWithImage(imageDto: ImageDto?): ImageDto {
		imageDto?.content ?: return imageService.getDefaultImage("default-wish")
		return imageService.saveImage(imageDto.content!!)
	}

	private fun getWishDto(wish: Wish) = mapper.map(wish, WishDto::class.java)
}