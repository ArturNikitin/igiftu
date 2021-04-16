package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.enums.Access
import com.svetka.igiftu.service.WishService
import java.nio.charset.StandardCharsets
import javax.persistence.EntityNotFoundException
import kotlin.test.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
internal class WishControllerTest {
	
	companion object {
		const val id: Long = 1L
	}
	
	@MockBean
	private lateinit var wishService: WishService
	
	@Autowired
	private lateinit var mockMvc: MockMvc
	
	@BeforeEach
	fun setUp() {
		Mockito.`when`(
			wishService.getWishById(id)
		).thenReturn(
			WishDto(
				id,
				name = "My Wish",
				price = 10.00,
				access = Access.PUBLIC.name
			)
		)
		Mockito.`when`(
			wishService.getWishById(2L)
		).thenThrow(EntityNotFoundException("Wish with ID 2 not found"))
	}
	
	@Test
	fun getWish() {
		mockMvc.perform(
			get("/wish/1")
		).andExpect(status().isOk)
		
		Mockito.verify(wishService, times(1)).getWishById(id)
	}
	
	@Test
	fun getWishEntityNotFound() {
		val contentAsString = mockMvc.perform(
			get("/wish/2")
		)
			.andExpect(status().isNotFound)
			.andReturn()
			.response.getContentAsString(StandardCharsets.UTF_8)
		
		assertEquals("Wish with ID 2 not found", contentAsString)
		
	}
	
	@Test
	fun createWish() {
		Mockito.`when`(
			wishService.createWish(wishDto())
		).thenReturn(
			wishDto()
		)
		
		
		val wishDto = "{\n" +
			"    \"name\": \"My first wish\",\n" +
			"    \"price\": 10.15,\n" +
			"    \"access\": \"PUBLIC\"\n" +
			"}"
		mockMvc.perform(
			MockMvcRequestBuilders.post("/wish")
				.content(wishDto)
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isCreated)
			.andExpect(
				MockMvcResultMatchers.content()
					.contentType(MediaType.APPLICATION_JSON)
			)
		
		Mockito.verify(wishService, times(1)).createWish(wishDto())
	}
	
	private fun wishDto() = WishDto(
		name = "My first wish",
		price = 10.15,
		access = Access.PUBLIC.name
	)
}