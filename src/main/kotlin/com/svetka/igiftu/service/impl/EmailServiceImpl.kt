package com.svetka.igiftu.service.impl

import com.svetka.igiftu.service.common.EmailService
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailServiceImpl(
	private val emailSender: JavaMailSender
) : EmailService {
	private val subjectOnRegistration = "Добро пожаловать от I GIFT YOU!"
	private val textOnRegistration = "Рады приветствовать вас на нашем сайте! Всегда получайте только желанные подарки!"

	override fun sendResetPasswordEmail(email: String, token: String) {
		TODO("Not yet implemented")
	}

	override fun sendEmail(email: String) {
		SimpleMailMessage()
			.apply {
				setFrom("igiftuwishlist@gmail.com")
				setTo(email)
				setSubject(subjectOnRegistration)
				setText(textOnRegistration)
			}
			.also { emailSender.send(it) }
	}
}