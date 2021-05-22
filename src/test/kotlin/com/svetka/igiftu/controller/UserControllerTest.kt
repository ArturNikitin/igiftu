package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.service.UserService
import com.svetka.igiftu.service.impl.WishServiceImplTest
import java.nio.charset.StandardCharsets
import javax.persistence.EntityNotFoundException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@WebMvcTest(UserController::class)
internal class UserControllerTest : AbstractControllerTest() {
	
	companion object {
		const val userId: Long = 1L
	}
	
	@MockBean
	private lateinit var userService: UserService
	
	@Autowired
	private lateinit var mockMvc: MockMvc
	
	
	@BeforeEach
	fun setUp() {
		`when`(
			userService.registerUser(
				UserCredentials(
					email = "bob@domain.com",
					password = "151516"
				)
			)
		).then {
			UserDto(
				id = 1L,
				email = "bob@domain.com",
				login = "@bob"
			)
		}
		
		`when`(
			userService.registerUser(
				UserCredentials(
					email = "bobdomain.com",
					password = "151516"
				)
			)
		).then {
			UserDto(
				id = 1L,
				email = "bobdomain.com",
				login = "@bob"
			)
		}
		
		`when`(
			userService.getUserById(userId)
		).then {
			UserDto(
				id = 1L,
				email = "bob@domain.com",
				login = "@bob"
			)
		}
		
		`when`(
			userService.getAllWishesByUserId(userId)
		).then {
			PayloadDto(true, listOf(WishServiceImplTest.getWishDto()))
		}
		
		`when`(
		userService.getAllWishesByUserId(2L)
		).thenThrow(EntityNotFoundException::class.java)
		
		`when`(
			userService.createWish(1L, WishServiceImplTest.createWishDto())
		).then {
			WishServiceImplTest.createWishDto()
		}
		
		`when`(
			userService.createWish(2L, WishServiceImplTest.createWishDto())
		).thenThrow(EntityNotFoundException::class.java)
	}
	
	@Test
	fun createWish() {
		val wish = "{\n\"name\": \"Create wish\"\n}"
		val response = mockMvc.perform(
			post("/user/$userId/wish")
				.content(wish)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isCreated)
			.andReturn().response.contentAsString
		
		assertNotNull(response)
		
		verify(userService, times(1)).createWish(userId, WishServiceImplTest.createWishDto())
	}
	
	@Test
	fun createWishUserNotFound() {
		val wish = "{\n\"name\": \"Create wish\"\n}"
		mockMvc.perform(
			post("/user/2/wish")
				.content(wish)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isNotFound)
		
		verify(userService, times(1)).createWish(2L, WishServiceImplTest.createWishDto())
	}
	
	@Test
	fun getWishesByUserIdUserFound() {
		val response = mockMvc.perform(
			get("/user/$userId/wish")
		).andExpect(status().isOk).andReturn().response.contentAsString
		
		assertNotNull(response)
		println(response)
		
		verify(userService, times(1)).getAllWishesByUserId(userId)
	}
	
	@Test
	fun getWishesByUserIdUserNotFound() {
		val response = mockMvc.perform(
			get("/user/2/wish")
		).andExpect(status().isNotFound).andReturn().response.contentAsString
		
		assertNotNull(response)
		println(response)
		
		verify(userService, times(1)).getAllWishesByUserId(2L)
	}
	
	@Test
	fun getUser() {
		val token = getToken("admin@gmail.com")
		
		val response = mockMvc.perform(
			get("/user/$userId").header("authorization", token)
		).andExpect(status().isOk)
			.andReturn().response.contentAsString
		
		assertNotNull(response)
		println(response)
		verify(userService, times(1)).getUserById(userId)
	}
	
	@Test
	fun createUser() {
	}
	
	@Test
	fun registerUserSuccessTest() {
		val user = "{\"email\" : \"bob@domain.com\", \"password\": \"151516\" }"
		mockMvc.perform(
			post("/user/registration")
				.content(user)
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isCreated)
			.andExpect(
				content()
					.contentType(MediaType.APPLICATION_JSON)
			)
	}
	
	@Test
	fun registerUserFailTestIncorrectEmail() {
		val user = "{\"email\" : \"bobdomain.com\", \"password\": \"151516\" }"
		val contentAsString = mockMvc.perform(
			post("/user/registration")
				.content(user)
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isBadRequest)
			.andExpect(
				content()
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andReturn()
			.response.getContentAsString(StandardCharsets.UTF_8)
		
		assertEquals("{\"email\":\"Неккоректный имейл\"}", contentAsString)
	}
}