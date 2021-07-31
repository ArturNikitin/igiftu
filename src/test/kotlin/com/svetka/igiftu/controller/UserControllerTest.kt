package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.service.UserService
import com.svetka.igiftu.service.impl.WishServiceImplTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.charset.StandardCharsets
import javax.persistence.EntityNotFoundException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


@WebMvcTest(UserController::class)
internal class UserControllerTest : AbstractControllerTest() {
	
	companion object {
		const val userId: Long = 1L
	}
	
	@MockBean
	private lateinit var userService: UserService

	@BeforeEach
	fun setUp() {
		`when`(
			userService.register(
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
			userService.register(
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
			userService.getAllWishes(userId)
		).then {
			PayloadDto(true, listOf(WishServiceImplTest.getWishDto()))
		}
		
		`when`(
		userService.getAllWishes(2L)
		).thenThrow(EntityNotFoundException::class.java)
	}

	@Test
	fun restorePassword() {
		val email = "{\n\"email\": \"artur@mail.com\"\n}"

		val response = mockMvc.perform(
			post("/user/password")
				.content(email)
				.contentType(MediaType.APPLICATION_JSON)
		).andExpect(status().isOk).andReturn().response.contentAsString

		assertNotNull(response)

		verify(userService, times(1)).resetPassword("artur@mail.com")
	}
	
	@Test
	fun getWishesByUserIdUserFound() {
		val response = mockMvc.perform(
			get("/user/$userId/wish")
		).andExpect(status().isOk).andReturn().response.contentAsString
		
		assertNotNull(response)
		println(response)
		
		verify(userService, times(1)).getAllWishes(userId)
	}
	
	@Test
	fun getWishesByUserIdUserNotFound() {
		val response = mockMvc.perform(
			get("/user/2/wish")
		).andExpect(status().isNotFound).andReturn().response.contentAsString
		
		assertNotNull(response)
		println(response)
		
		verify(userService, times(1)).getAllWishes(2L)
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