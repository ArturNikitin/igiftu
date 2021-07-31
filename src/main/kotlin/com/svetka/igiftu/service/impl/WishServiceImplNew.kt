package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.repository.UserRepository
import com.svetka.igiftu.repository.WishRepository
import com.svetka.igiftu.service.WishService
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service
import java.security.Principal
import java.time.LocalDateTime

@Service
class WishServiceImplNew(
	private val wishRepository: WishRepository,
	private val mapper: MapperFacade
) : WishService {
	override fun create(wishDto: WishDto, user: User): WishDto {

//		val wish = mapper.map(wishDto, Wish::class.java).apply {
//			createdDate = LocalDateTime.now()
//			lastModifiedDate = LocalDateTime.now()
//			user = user
//		}
//		return mapWishToDto(wishRepository.save(wish))
		return WishDto(
			name = "M",
			price = 10.0,
			access = "PRIVATE"
		)
	}

	override fun delete(wishId: Long) {
		TODO("Not yet implemented")
	}

	override fun update(contentId: Long, content: WishDto): WishDto {
		TODO("Not yet implemented")
	}

	override fun getWishesByUserId(userId: Long): List<WishDto> {
		TODO("Not yet implemented")
	}

	override fun getWishesCountByUserId(userId: Long): Long {
		TODO("Not yet implemented")
	}

	override fun getWishById(wishId: Long): WishDto {
		TODO("Not yet implemented")
	}

	override fun create(wishDto: WishDto): WishDto {
		TODO("Not yet implemented")
	}


	override fun delete(wishId: Long, principal: Principal) {
		throw RuntimeException()
	}

	private fun mapWishToDto(wish: Wish) = mapper.map(wish, WishDto::class.java)

}