package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.repository.WishRepository
import com.svetka.igiftu.service.WishService
import javax.persistence.EntityNotFoundException
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WishServiceImpl(
	private val wishRepository: WishRepository,
	private val mapper: MapperFacade
) : WishService {
	
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
	
//	пока просто заглушка
	override fun createWish(wishDto: WishDto): WishDto {
		return WishDto(
			id = 1L,
			name = "M",
			price = 10.0,
			access = "PRIVATE"
		)
	}
	
	@Transactional
	override fun getWishesCountByUserId(userId: Long): Long = wishRepository.countByUserId(userId)
	
	private fun mapWishToDto(wish: Wish) = mapper.map(wish, WishDto::class.java)
}