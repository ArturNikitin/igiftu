package com.svetka.igiftu.service.impl

import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EmailServiceTest {
	
	@InjectMockKs
	private lateinit var emailService: EmailServiceImpl
	
	@BeforeEach
	fun before() {
		MockKAnnotations.init(this)
	}
	
	@Test
	fun sendEmailTest() {
		emailService.sendEmail("String")
	}
}