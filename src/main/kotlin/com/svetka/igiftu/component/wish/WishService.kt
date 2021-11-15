package com.svetka.igiftu.component.wish

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.repository.WishRepository
import ma.glasnost.orika.MapperFacade
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
internal class WishService(
	private val wishRepository: WishRepository,
	private val mapper: MapperFacade
) : WishComponent {

	private val logger = KotlinLogging.logger {  }

	override fun getWishById(id: Long): WishDto {
		TODO("Not yet implemented")
	}

	override fun getWishesById(ids: Set<Long>): Iterable<WishDto> {
		logger.debug { "Getting wishes {$ids}" }
		val wishes = wishRepository.findAllById(ids).map(::getWishDto)
		logger.debug { "Got wishes $wishes" }
		return wishes
	}

	private fun getWishDto(wish: Wish) = mapper.map(wish, WishDto::class.java)
}