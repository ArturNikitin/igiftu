package com.svetka.igiftu.service.impl

import com.svetka.igiftu.service.SecurityManager
import com.svetka.igiftu.service.UserService
import com.svetka.igiftu.service.UserTest
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class SecurityManagerImplTest : UserTest() {

//	@MockK
//	private lateinit var userService: UserService

	@InjectMockKs
	private lateinit var securityManager: SecurityManager

	@Test
	fun isModificationAllowedSuccess() {
	}

	@Test
	fun isModificationAllowedFailure() {

	}

	@Test
	fun isCreationAllowed() {
	}

	@Test
	fun isOwner() {
	}
}