package com.svetka.igiftu.service.impl

import com.svetka.igiftu.service.EmailService
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.Message
import javax.mail.Multipart
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
    private val textOnRegistration = "Спасибо, что потребляешь осознанно и стремишься получать только нужные подарки, с чем мы тебе и поможем!\n" +
        "\n" +
        "Чтобы создать списки подарков, которые твои друзья потом смогут забронировать, нажимай на кнопку ниже.\n" +
        "\n" +
        "< Перейти к созданию списка желанных подарков >\n" +
        "\n" +
        "Надеемся, что тебе будет легко и удобно пользоваться нашим сервисом. А если что-то пойдет не так, с любыми вопросами и предложениями можно писать на почту igiftyou.ru@gmail.com.\n" +
        "\n" +
        "Твоя команда I GIFT YOU!"
    override fun sendEmail(email: String) {
        val multipart = MimeMultipart("related")
        val bodyPart = MimeBodyPart()
        val imagePart = MimeBodyPart()
        try {
            val content = Files.readString(Paths.get("src/main/resources/static/welcomingEmail.html"))
            bodyPart.setContent(content, "text/html; charset=UTF-8")
            val dataSource = FileDataSource("src/main/resources/static/pictures/img.png")
            imagePart.dataHandler = DataHandler(dataSource)
            imagePart.setHeader("Content-ID", "<image>")
            imagePart.description = MimeBodyPart.INLINE
        } catch (ex: Exception) {
            logger.error { "${ex.message}" }
            bodyPart.setContent(htmlText, "text/html; charset=UTF-8")
        }

        multipart.addBodyPart(bodyPart)
        multipart.addBodyPart(imagePart)

        try {
            logger.info { "Sending email to $email" }
            mailSender.createMimeMessage().apply {
                setFrom("igiftuwishlist@gmail.com")
                subject = subjectOnRegistration
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

    private val htmlText = "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
        "    <meta charset=\"UTF-8\">\n" +
        "</head>\n" +
        "<body>\n" +
        "<div id=\"header\" style=\"background-color: #eeeeee\">\n" +
        "    <div align=\"center\">\n" +
        "        <p><em>Добро пожаловать!</em></p>\n" +
        "        <br>\n" +
        "        <p>Спасибо, что потребляешь осознанно и стремишься получать только нужные подарки, с чем мы тебе и поможем!</p>\n" +
        "    </div>\n" +
        "</div>\n" +
        "</body>\n" +
        "</html>"
}