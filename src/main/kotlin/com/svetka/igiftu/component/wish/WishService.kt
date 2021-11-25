package com.svetka.igiftu.component.wish

import com.svetka.igiftu.component.user.UserComponent
import com.svetka.igiftu.dateTimeFormat
import com.svetka.igiftu.dto.ImageDto
import com.svetka.igiftu.dto.UserInfo
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Image
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.entity.enums.Access
import com.svetka.igiftu.exceptions.SecurityModificationException
import com.svetka.igiftu.service.ImageService
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

	@Transactional
	override fun getWishesById(ids: Set<Long>): Iterable<WishDto> {
		logger.debug { "Getting wishes {$ids}" }
		val wishes = wishRepository.findAllById(ids).map(::getWishDto)
		logger.debug { "Got wishes $wishes" }
		return wishes
	}

	@Transactional
	override fun createWish(user: UserInfo, requestWish: WishDto): WishDto {
		val createdDate = LocalDateTime.now().format(dateTimeFormat)
		val wish = requestWish.apply {
			this.createdDate = createdDate
			lastModifiedDate = createdDate
		}.let { mapper.map(it, Wish::class.java) }

		wish.image = requestWish.image?.let(::dealWithImage)

		return userService.addWishes(user.id, setOf(wish))
			.filter { it.lastModifiedDate == createdDate }
			.map { mapper.map(it, WishDto::class.java) }
			.first()
	}

	@Transactional
	override fun updateWish(user: UserInfo, requestWish: WishDto): WishDto {
		if (!isOperationAllowed(user.id, user.username))
			throw SecurityModificationException("Illegal modification attempt by user [${user.username}]")

		val updatedWish = getWishIfExists(requestWish.id ?: 0L)
			.also {
				it.access = Access.valueOf(requestWish.access)
				it.name = requestWish.name
				it.price = requestWish.price
				it.lastModifiedDate = LocalDateTime.now()
				it.isAnalogPossible = requestWish.isAnalogPossible
				it.isBooked = requestWish.isBooked
				it.isCompleted = requestWish.isCompleted
				it.image = requestWish.image?.let { image -> dealWithImage(image) }
			}.let { wishRepository.save(it) }

		return getWishDto(updatedWish)
	}

	@Transactional
	override fun deleteWish(user: UserInfo, wishId: Long) {
		if (!isOperationAllowed(user.id, user.username))
			throw SecurityModificationException("Illegal modification attempt by user [${user.username}]")
		getWishIfExists(wishId)
			.also { wishRepository.deleteById(it.id!!) }
	}



	private fun getWishIfExists(wishId: Long) =
		wishRepository.findById(wishId)
			.orElseThrow { EntityNotFoundException("Wish [$wishId] not found") }

	private fun dealWithImage(imageDto: ImageDto): Image =
		imageService.uploadImage(imageDto.content!!)
			.let { mapper.map(it, Image::class.java) }


	private fun getWishDto(wish: Wish) = mapper.map(wish, WishDto::class.java)

	private fun isOperationAllowed(userId: Long, username: String) =
		userService.isSameUser(userId, username)
}