package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.service.ContentCreationManager
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(WishController::class)
internal class WishControllerTest : AbstractControllerTest() {

	object MockitoHelper {
		fun <T> anyObject(): T {
			any<T>()
			return uninitialized()
		}

		@Suppress("UNCHECKED_CAST")
		fun <T> uninitialized(): T = null as T
	}

	companion object {
		const val id: Long = 1L
		const val wishDtoNoId = "{\n" +
				"    \"name\": \"My test wish\",\n" +
				"    \"price\": 10.15,\n" +
				"    \"access\": \"PUBLIC\"\n" +
				"}"
		const val wishDtoWithId = "{\n" +
				"    \"id\": \"1\",\n" +
				"    \"name\": \"My test wish\",\n" +
				"    \"price\": 10.15,\n" +
				"    \"access\": \"PUBLIC\"\n" +
				"}"
	}

	@MockBean
	private lateinit var creationManager: ContentCreationManager

	@BeforeEach
	fun setUp() {
		`when`(
			creationManager.create(anyLong(), MockitoHelper.anyObject(), MockitoHelper.anyObject())
		).thenReturn(
			wishDtoWithId()
		)

		`when`(
			creationManager.update(anyLong(), anyLong(), MockitoHelper.anyObject(), MockitoHelper.anyObject())
		).thenReturn(
			wishDtoWithId()
		)
	}

	// GET TODO

	// POST
	@Test
	fun createWishSuccess() {
		val token = getToken("user@gmail.com")

		mockMvc.perform(
			MockMvcRequestBuilders.post("/user/1/wish")
				.header("authorization", token)
				.content(wishDtoNoId)
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isCreated)
			.andReturn()
			.response
			.also { Assertions.assertTrue(it.contentAsString.subSequence(6, 7) == id.toString()) }

		verify(creationManager, times(1))
			.create(anyLong(), MockitoHelper.anyObject(), MockitoHelper.anyObject())
	}

//	PUT TODO

	@Test
	fun updateWishSuccess() {
		val token = getToken("user@gmail.com")

		mockMvc.perform(
			MockMvcRequestBuilders.put("/user/1/wish/1")
				.header("authorization", token)
				.content(wishDtoWithId)
				.contentType(MediaType.APPLICATION_JSON)
		)
			.andExpect(status().isAccepted)
			.andReturn()
			.response
			.also { Assertions.assertTrue(it.contentAsString.subSequence(6, 7) == id.toString()) }

		verify(creationManager, times(1))
			.update(anyLong(), anyLong(), MockitoHelper.anyObject(), MockitoHelper.anyObject())
	}

//	PATCH TODO

	private fun wishDtoWithId() = WishDto(
		id = 1L,
		name = "My test wish",
		price = 10.15,
		access = "PUBLIC",
		createdDate = "created date",
		lastModifiedDate = "last modified date",
		isBooked = false,
		isAnalogPossible = true,
		isCompleted = false
	)

}