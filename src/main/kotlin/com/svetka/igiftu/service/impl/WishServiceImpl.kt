package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.repository.WishRepository
import com.svetka.igiftu.service.WishService
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WishServiceImpl(
	private val wishRepository: WishRepository,
	private val mapper: MapperFacade
) : WishService {
	
	@Transactional
	override fun getWishesByUserId(id: Long): List<WishDto> {
		val allByUserId = wishRepository.getAllByUserId(id)
		return allByUserId.map { mapWishToDto(it) }
	}
	
	@Transactional
	override fun getWishesCountByUser(id: Long): Long = wishRepository.countByUserId(id)
	
	private fun mapWishToDto(wish: Wish) = mapper.map(wish, WishDto::class.java)
}