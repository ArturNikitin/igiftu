package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.enums.Access
import com.svetka.igiftu.service.ContentManager
import com.svetka.igiftu.service.WishService
import org.junit.Ignore
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.nio.charset.StandardCharsets
import javax.persistence.EntityNotFoundException
import kotlin.test.assertEquals

@WebMvcTest(WishController::class)
internal class WishControllerTest : AbstractControllerTest() {

	companion object {
		const val id: Long = 1L
		const val wishDto = "{\n" +
				"    \"name\": \"My first wish\",\n" +
				"    \"price\": 10.15,\n" +
				"    \"access\": \"PUBLIC\"\n" +
				"}"
	}
	
	@MockBean
	private lateinit var wishService: WishService

	@MockBean
	private lateinit var contentManger: ContentManager
	
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

	// GET
//	@Test
//	fun getWish() {
//		mockMvc.perform(
//			get("/wish/1")
//		).andExpect(status().isOk)
//
//		Mockito.verify(wishService, times(1)).getWishById(id)
//	}
//
//	@Test
//	fun getWishEntityNotFound() {
//		val contentAsString = mockMvc.perform(
//			get("/wish/2")
//		)
//			.andExpect(status().isNotFound)
//			.andReturn()
//			.response.getContentAsString(StandardCharsets.UTF_8)
//
//		assertEquals("Wish with ID 2 not found", contentAsString)
//
//	}

	// POST
//	@Test
//	fun createWish() {
//		Mockito.`when`(
//			wishService.create(, wishDto())
//		).thenReturn(
//			wishDto()
//		)
//		mockMvc.perform(
//			MockMvcRequestBuilders.post("/wish")
//				.content(wishDto)
//				.contentType(MediaType.APPLICATION_JSON)
//		)
//			.andExpect(status().isCreated)
//			.andExpect(
//				MockMvcResultMatchers.content()
//					.contentType(MediaType.APPLICATION_JSON)
//			)
//
//		Mockito.verify(wishService, times(1)).create(, wishDto())
//	}

//	PUT

//	@Test
//	fun updateWishSuccessTest() {
//		val token = getToken("user@gmail.com")
//
//		mockMvc.perform(
//			put("/user/1/wish/1")
//				.header("authorization", token)
//				.content(wishDto)
//				.contentType(MediaType.APPLICATION_JSON)
//		).andExpect(status().isAccepted)
//	}
	
	private fun wishDto() = WishDto(
		name = "My first wish",
		price = 10.15,
		access = Access.PUBLIC.name
	)
}