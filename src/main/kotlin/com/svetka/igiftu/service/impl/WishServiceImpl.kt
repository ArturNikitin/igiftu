package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.exceptions.ItemDoesNotBelongToUser
import com.svetka.igiftu.repository.WishRepository
import com.svetka.igiftu.service.WishService
import javax.persistence.EntityNotFoundException
import ma.glasnost.orika.MapperFacade
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.Principal

@Service
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
	@Transactional
	override fun createWish(wishDto: WishDto): WishDto {
		return WishDto(
			id = 1L,
			name = "M",
			price = 10.0,
			access = "PRIVATE"
		)
	}

	override fun deleteWish(wishId: Long, principal: Principal) {
		checkUser(wishId, principal.name)
		log.info { "Deleting wish with id $wishId" }
		wishRepository.deleteById(wishId)
	}

	private fun checkUser(wishId: Long, userName: String) = checkName(wishRepository.findById(wishId)
				.orElseThrow{ EntityNotFoundException("Wish with id $wishId not found") }.user,
			userName)

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