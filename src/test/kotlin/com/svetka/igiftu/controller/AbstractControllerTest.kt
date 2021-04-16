package com.svetka.igiftu.controller

import com.svetka.igiftu.TestConfiguration
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

@ExtendWith(SpringExtension::class)
@Import(TestConfiguration::class)
abstract class AbstractControllerTest {
	
	private val jwtPath = "/login"
	
	@Autowired
	private lateinit var mockMvc: MockMvc
	
	protected fun getToken(email: String): String {
		val user = "{\"email\" : \"$email\", \"password\": \"123\" }"
		
		return mockMvc.perform(
			MockMvcRequestBuilders.post(jwtPath)
				.content(user)
				.contentType(MediaType.APPLICATION_JSON)
		).andReturn()
			.response
			.getHeaderValue("authorization")
			as String
	}
}