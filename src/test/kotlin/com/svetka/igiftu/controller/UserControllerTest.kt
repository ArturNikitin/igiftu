package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.service.UserService
import java.nio.charset.Charset
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
internal class UserControllerTest {
	
	@MockBean
	private lateinit var userService: UserService
	
	@Autowired
	private lateinit var userController: UserController
	
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
	}
	
	@Test
	fun getUser() {
	}
	
	@Test
	fun createUser() {
	}
	
	@Test
	fun registerUser() {
//		val textPlainUtf8 = MediaType(MediaType.TEXT_PLAIN, Charset.forName("UTF-8"))
		val user = "{\"email\" : \"bob@domain.com\", \"password\": \"151516\" }"
		mockMvc.perform(
			MockMvcRequestBuilders.post("/user/registration")
				.content(user)
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(MockMvcResultMatchers.status().isCreated)
			.andExpect(
				MockMvcResultMatchers.content()
					.contentType(MediaType.APPLICATION_JSON)
			)
	}
}