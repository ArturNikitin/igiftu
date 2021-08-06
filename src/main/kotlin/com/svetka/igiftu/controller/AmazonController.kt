package com.svetka.igiftu.controller

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import com.svetka.igiftu.dto.ImageDto
import com.svetka.igiftu.service.common.StorageService
import com.svetka.igiftu.service.common.impl.S3StorageService
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/aws")
@CrossOrigin
class AmazonController(
	private val amazonClient: AmazonS3,
	private val s3StorageService: StorageService
) {

	private val logger = KotlinLogging.logger {  }
	private val bucketName = "i-gift-u-wish-test-bucket"
	private val testFileName = "default-wish"

	@GetMapping
	fun get() : ResponseEntity<ByteArray> {
		logger.info { "Received request to get" }
		val s3Object = amazonClient.getObject(bucketName, testFileName)
		val objectContent = s3Object.objectContent
		val readAllBytes = objectContent.readAllBytes()
		logger.info { "Finished request to get" }
		val header = HttpHeaders().apply {
			contentType = MediaType.IMAGE_JPEG
		}

		return ResponseEntity(readAllBytes, header, HttpStatus.OK)
	}

	@GetMapping("/bytes")
	fun getBytes() : ResponseEntity<ImageDto> {
		logger.info { "Received request to get" }
		val s3Object = amazonClient.getObject(bucketName, testFileName)
		val objectContent = s3Object.objectContent
		val readAllBytes = objectContent.readAllBytes()

		val header = HttpHeaders().apply {
			contentType = MediaType.APPLICATION_JSON
		}

		return ResponseEntity(ImageDto.fill(readAllBytes), header, HttpStatus.OK)
	}

	@PostMapping
	fun create() {
		logger.info { "Received request to create" }
		val file = File("src/main/resources/static/pictures/wish.jpeg")
		val fileInputStream = FileInputStream(file)
		val byteArrayInputStream = ByteArrayInputStream(fileInputStream.readAllBytes())
		val putObject = amazonClient.putObject(
			bucketName,
			testFileName,
			byteArrayInputStream,
			ObjectMetadata()
		)
		logger.info { "Finished request to create" }
	}

	@PostMapping("/test")
	fun testCreate(
		@RequestBody image: ImageDto
	) {
		logger.info { "Received request to create" }
		s3StorageService.putFile(image.content!!.toByteArray(), "test")
		logger.info { "Finished request to create" }

	}

}