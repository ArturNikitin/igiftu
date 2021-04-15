package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.security.service.UserDetailsServiceImpl
import com.svetka.igiftu.service.UserService
import io.mockk.verify
import java.nio.charset.StandardCharsets
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.verification.VerificationMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerTest {
	
	companion object {
		const val id: Long = 1L
	}
	
	@MockBean
	private lateinit var userService: UserService
	
	@Autowired
	private lateinit var mockMvc: MockMvc
	
	
	@BeforeEach
	fun setUp() {
		Mockito.`when`(
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
		
		Mockito.`when`(
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
		
		Mockito.`when`(
			userService.getUserById(id)
		).then {
			UserDto(
				id = 1L,
				email = "bob@domain.com",
				login = "@bob"
			)
		}
	}
	
	@Test
	fun getUser() {
		mockMvc.perform(
			get("/user/$id")
		).andExpect(status().isOk)
		
		Mockito.verify(userService, times(1)).getUserById(id)
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