package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.enums.Access
import com.svetka.igiftu.service.WishService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
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
	}
	
	@Test
	fun getWish() {
		mockMvc.perform(
			get("/wish/1")
		).andExpect(status().isOk)
		
		Mockito.verify(wishService, times(1)).getWishById(id)
	}
}