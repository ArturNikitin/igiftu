package com.svetka.igiftu.service.impl

import com.svetka.igiftu.service.SecurityManager
import com.svetka.igiftu.service.UserTest
import io.mockk.impl.annotations.InjectMockKs
import org.junit.jupiter.api.Test

internal class SecurityManagerImplTest : UserTest() {

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