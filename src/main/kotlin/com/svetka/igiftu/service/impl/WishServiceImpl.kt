package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.exceptions.ItemDoesNotBelongToUser
import com.svetka.igiftu.repository.WishRepository
import com.svetka.igiftu.service.WishService
import ma.glasnost.orika.MapperFacade
import mu.KotlinLogging
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal
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

	private val log = KotlinLogging.logger {  }
	
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

//	TODO пока просто заглушка
	override fun create(wishDto: WishDto): WishDto {
		return createOrUpdate(wishDto)
	}

	override fun create(wishDto: WishDto, user: User): WishDto {
		return createOrUpdate(wishDto)
	}

	@Transactional
	override fun delete(wishId: Long, principal: Principal) {
		checkUser(wishId, principal.name)
		log.info { "Deleting wish with id $wishId" }
		wishRepository.deleteById(wishId)
	}

	@Transactional
	override fun delete(wishId: Long) =
		wishRepository.deleteById(wishId)

//	TODO
	@Transactional
	override fun update(contentId: Long, content: WishDto): WishDto {
		return	createOrUpdate(content)
	}

//	TODO
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

	private fun checkUser(wishId: Long, userName: String) = true

	private fun checkName(userByWishId: User, userName: String) {
		if ( userName != userByWishId.email && userName != userByWishId.login) {
			throw ItemDoesNotBelongToUser("user with userName $userName is trying to access wish that belongs to " +
					"${userByWishId.login}/ ${userByWishId.email}")
		}
	}

	@Transactional
	override fun getWishesCountByUserId(userId: Long): Long = wishRepository.countByUserId(userId)

	private fun mapWishToDto(wish: Wish) = mapper.map(wish, WishDto::class.java)
}