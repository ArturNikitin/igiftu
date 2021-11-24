package com.svetka.igiftu.service.impl

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import com.svetka.igiftu.service.StorageService
import java.io.ByteArrayInputStream
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class S3StorageService(
	private val amazonClient: AmazonS3
) : StorageService {

	private val logger = KotlinLogging.logger { }

	private val bucketName = "i-gift-u-wish-test-bucket"

	override fun putFile(content: ByteArray, fileName: String): String {
		logger.info { "Received request to save $fileName with data $content" }
		return try {
			amazonClient.putObject(
				bucketName,
				fileName,
				ByteArrayInputStream(content),
				ObjectMetadata()
			)
			logger.info { "File $fileName was saved" }
			fileName
		} catch (ex: Exception) {
			logger.error { "File was not saved" }
			throw ex
		}
	}

	override fun getFile(fileName: String): ByteArray =
		try {
			val s3File = amazonClient.getObject(bucketName, fileName)
			logger.info { "Got file $fileName from storage" }

			s3File.objectContent.readAllBytes()
		} catch (ex: Exception) {
			logger.error { "File was not read" }
			throw ex
		}

	override fun deleteFile(fileName: String): Unit =
		try {
			amazonClient.deleteObject(bucketName, fileName)
			logger.info { "Deleted file $fileName from storage" }
		} catch (ex: Exception) {
			logger.error { "File was not read" }
			throw ex
		}
}