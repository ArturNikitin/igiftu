package com.svetka.igiftu.service.impl

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

@Service
@Primary
class MimeEmailServiceImpl(
        private val mailSender: JavaMailSender
) : EmailService {
    private val logger = KotlinLogging.logger { }
    private val subjectOnRegistration = "Привет и добро пожаловать в I GIFT YOU!"

    //    TODO REFACTORING
    override fun sendResetPasswordEmail(email: String, token: String) {
        logger.info { "Preparing data to send email to $email" }
        val multipart = MimeMultipart()
        val bodyPart = MimeBodyPart()
        val linkToResetPassword = "http://localhost:8081/user/password/update?token=$token"

        try {
            val content = Files.readString(Paths.get("src/main/resources/static/reset-password-email.html"))
            val updatedContent = content.replace("url/resetpassword", linkToResetPassword)
            bodyPart.setContent(updatedContent, "text/html; charset=UTF-8")
        } catch (ex: Exception) {
            logger.error { "${ex.message}" }
        }

        multipart.addBodyPart(bodyPart)

        sendEmail(email, multipart, "Сбросить пароль I GIFT YOU")
    }

    override fun sendWelcomingEmail(email: String) {
        logger.info { "Preparing data to send email to $email" }
        val multipart = MimeMultipart("related")
        val bodyPart = MimeBodyPart()
        val imagePart = MimeBodyPart()
        try {
            val content = Files.readString(Paths.get("src/main/resources/static/welcoming-email.html"))
            bodyPart.setContent(content, "text/html; charset=UTF-8")
            val dataSource = FileDataSource("src/main/resources/static/pictures/img.png")
            imagePart.dataHandler = DataHandler(dataSource)
            imagePart.setHeader("Content-ID", "<image>")
            imagePart.description = MimeBodyPart.INLINE
        } catch (ex: Exception) {
            logger.error { "${ex.message}" }
        }

        multipart.addBodyPart(bodyPart)
        multipart.addBodyPart(imagePart)

        sendEmail(email, multipart, subjectOnRegistration)
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