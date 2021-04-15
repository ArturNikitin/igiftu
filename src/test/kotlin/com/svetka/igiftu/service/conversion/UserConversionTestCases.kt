package com.svetka.igiftu.service.conversion

import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.service.impl.WishServiceImplTest
import java.time.LocalDateTime
import ma.glasnost.orika.MapperFacade
import ma.glasnost.orika.MapperFactory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test


class UserConversionTestCases {
	
	companion object {
		lateinit var factory: MapperFactory
		lateinit var mapper: MapperFacade
		
		@BeforeAll
		@JvmStatic
		internal fun beforeAll() {
			factory = MappingUtils.getMapperFactory()
			mapper = factory.mapperFacade
		}
	}
	
	@Test
	fun userToUserDto() {
		val mappedUser = mapper.map(getUser(), UserDto::class.java)
		
		assertEquals("email@gmail.com", mappedUser.email)
		assertEquals("login", mappedUser.login)
	}
	
	@Test
	fun userDtoToUser() {
        val mappedUser = mapper.map(userDto(), User::class.java)
        val mappedCreateUser = mapper.map(createUserDto(), User::class.java)
        
        assertEquals(userDto().email, mappedUser.email)
        assertEquals(userDto().login, mappedUser.login)
        assertEquals(userDto().id, mappedUser.id)
		
		assertEquals(createUserDto().email, mappedCreateUser.email)
	}
	
	@Test
	fun wishToWishDto() {
		val wish = WishServiceImplTest.getWishDto()
		val mappedWish = mapper.map(wish, WishDto::class.java)
		
		assertEquals(wish.name, mappedWish.name)
	}
	
	@Test
	fun wishDtoToWish() {
		val wishDto = WishServiceImplTest.getWishDto()
		val mappedWish = mapper.map(wishDto, Wish::class.java)
		assertEquals(
			wishDto.name,
			mappedWish.name
		)
	}
	
	private fun getUser() = User(
		1L,
		LocalDateTime.now(),
		"1234",
		"email@gmail.com",
		"login",
		UserRoles.ROLE_USER,
		isEnabled = true,
		isAccountNonLocked = true
	)
	
	private fun userDto() = UserDto(
		1L,
		email = "email@gmail.com",
		login = "login"
	)
    
    private fun createUserDto() = UserDto(
	    email = "email@gmail.com"
    )
}