package com.svetka.igiftu.service.common.impl

import com.svetka.igiftu.service.common.EmailService
import java.nio.file.Files
import java.nio.file.Paths
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Message
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMultipart
import mu.KotlinLogging
import org.springframework.context.annotation.Primary
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger { }

@Service
@Primary
class MimeEmailService(
	private val mailSender: JavaMailSender
) : EmailService {
	private companion object {

		@JvmStatic
		val resetPasswordHtml: String by lazy { prepareResetPasswordHTMLForm() }

		@JvmStatic
		val welcomingHtml: String by lazy { prepareWelcomingHtmlForm() }

		@JvmStatic
		val welcomingImageBodyPart by lazy {
			MimeBodyPart().apply {
				logger.info { "Preparing image" }
				val dataSource = FileDataSource("src/main/resources/static/pictures/img.png")
				dataHandler = DataHandler(dataSource)
				addHeader("Content-ID", "<image>")
				description = MimeBodyPart.INLINE
			}
		}

		@JvmStatic
		private fun prepareResetPasswordHTMLForm() =
			Files.readString(Paths.get("src/main/resources/static/reset-password-email.html"))

		@JvmStatic
		private fun prepareWelcomingHtmlForm() =
			Files.readString(Paths.get("src/main/resources/static/welcoming-email.html"))
	}

	override fun sendResetPasswordEmail(email: String, token: String) {
		logger.info { "Preparing data to send email to $email" }

		val linkToResetPassword = "http://localhost:8081/user/password/update?token=$token"
		val content = resetPasswordHtml
		val updatedContent = content.replace("url/resetpassword", linkToResetPassword)

		sendEmailWithContent(email, updatedContent, "Сбросить пароль I GIFT YOU")
	}

	override fun sendWelcomingEmail(email: String) {
		logger.info { "Preparing data to send welcoming email to $email" }
		sendEmailWithInlinedImage(
			email, welcomingHtml, welcomingImageBodyPart, "Привет и добро пожаловать в I GIFT YOU!"
		)
	}

	private fun sendEmailWithInlinedImage(
		email: String,
		content: String,
		imageBodyPart: MimeBodyPart,
		emailSubject: String
	) {
		val multipart = MimeMultipart("related")
		val bodyPart = MimeBodyPart()
		bodyPart.setContent(content, "text/html; charset=UTF-8")
		multipart.addBodyPart(bodyPart)
		multipart.addBodyPart(imageBodyPart)

		sendEmail(email, multipart, emailSubject)
	}

	private fun sendEmailWithContent(email: String, content: String, emailSubject: String) {
		val multipart = MimeMultipart()
		val bodyPart = MimeBodyPart()

		bodyPart.setContent(content, "text/html; charset=UTF-8")
		multipart.addBodyPart(bodyPart)

		sendEmail(email, multipart, emailSubject)
	}

	private fun sendEmail(email: String, multipart: MimeMultipart, emailSubject: String) {
		try {
			logger.info { "Sending email to $email" }
			mailSender.createMimeMessage().apply {
				setFrom("igiftuwishlist@gmail.com")
				subject = emailSubject
				setContent(multipart)
				addRecipients(Message.RecipientType.TO, InternetAddress.parse(email))
			}.also {
				mailSender.send(it)
				logger.info { "Message sent to $email" }
			}
		} catch (ex: Exception) {
			logger.error { "${ex.message} \nWas not able to send email to $email" }
		}
	}
}