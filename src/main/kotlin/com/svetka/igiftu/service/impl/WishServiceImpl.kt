package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.entity.enums.Access
import com.svetka.igiftu.exceptions.SecurityException
import com.svetka.igiftu.repository.WishRepository
import com.svetka.igiftu.service.WishService
import ma.glasnost.orika.MapperFacade
import mu.KotlinLogging
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.EntityNotFoundException

private val format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")

@Service
@Primary
class WishServiceImpl(
	private val wishRepository: WishRepository,
	private val mapper: MapperFacade
) : WishService {

	private val log = KotlinLogging.logger { }

	@Transactional
	override fun getWishesByUserId(userId: Long): List<WishDto> {
		val allByUserId = wishRepository.getAllByUserId(userId)
		return allByUserId.map { mapWishToDto(it) }
	}

	@Transactional
	override fun getWishById(wishId: Long): WishDto = mapWishToDto(
		wishRepository.findById(wishId).orElseThrow {
			EntityNotFoundException("Wish with ID $wishId not found")
		}
	)

	override fun prepareForCreation(wishDto: WishDto): WishDto {
		return createOrUpdate(wishDto)
	}

	@Transactional
	override fun delete(wishId: Long) = wishRepository.deleteById(wishId)

	@Transactional
	override fun update(contentId: Long, content: WishDto): WishDto {
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

	fun createOrUpdate(wishDto: WishDto): WishDto {
	val createdDate = LocalDateTime.now().format(format)
	return WishDto(
			name = wishDto.name,
			price = wishDto.price,
			access = wishDto.access,
			createdDate = createdDate,
			lastModifiedDate = createdDate
		)
	}

	@Transactional
	override fun getWishesCountByUserId(userId: Long): Long = wishRepository.countByUserId(userId)

	private fun mapWishToDto(wish: Wish) = mapper.map(wish, WishDto::class.java)
}