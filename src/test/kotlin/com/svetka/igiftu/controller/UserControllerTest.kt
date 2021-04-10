package com.svetka.igiftu.controller

import com.svetka.igiftu.service.UserService
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class UserControllerTest {
	
	@MockK
	private lateinit var userService: UserService
	
	@InjectMockKs
	private lateinit var userController: UserController
	
	@BeforeEach
	fun setUp() {
		MockKAnnotations.init(this)
	}
	
	@Test
	fun getUser() {
	}
	
	@Test
	fun createUser() {
	}
	
	@Test
	fun registerUser() {
	}
}