package com.svetka.igiftu.service.impl

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender

class EmailServiceTest {
	
	@MockK
	private lateinit var emailSender: JavaMailSender
	
	@InjectMockKs
	private lateinit var emailService: EmailServiceImpl
	
	private val email = "test@email.com"
	private val subjectOnRegistration = "Добро пожаловать от I GIFT YOU!"
	private val textOnRegistration = "Рады приветствовать вас на нашем сайте! Всегда получайте только желанные подарки!"
	
	
	@BeforeEach
	fun before() {
		MockKAnnotations.init(this)
		every { emailSender.send(SimpleMailMessage().apply {
			setFrom("igiftuwishlist@gmail.com")
			setTo(email)
			setSubject(subjectOnRegistration)
			setText(textOnRegistration)
		})} returns Unit
	}
	
	@Test
	fun sendEmailTest() {
		emailService.sendWelcomingEmail(email)
		
		verify(exactly = 1) { emailSender.send(SimpleMailMessage().apply {
			setFrom("igiftuwishlist@gmail.com")
			setTo(email)
			setSubject(subjectOnRegistration)
			setText(textOnRegistration)
		}) }
	}
}