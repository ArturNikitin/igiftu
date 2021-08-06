package com.svetka.igiftu.service.common.impl

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectResult
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectInputStream
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import java.io.ByteArrayInputStream
import org.apache.http.client.methods.HttpPost
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.testcontainers.shaded.org.bouncycastle.util.Arrays

internal class S3StorageServiceTest {

	@MockK
	private lateinit var s3client: AmazonS3

	@InjectMockKs
	private lateinit var service: S3StorageService

	@BeforeEach
	fun setUp() {
		MockKAnnotations.init(this)
	}

	@Test
	fun putFile() {
		every {
			s3client.putObject(bucket, any(), any<ByteArrayInputStream>(), any())
		} returns PutObjectResult()

		val result = service.putFile(byteArray, fileName)

		Assert.assertEquals(fileName, result)
	}

	@Test
	fun getFile() {
		every {
			s3client.getObject(bucket, fileName)
		} returns S3Object().also {
			it.bucketName = bucket
			it.objectContent = S3ObjectInputStream(
				ByteArrayInputStream(byteArray),
				HttpPost())
		}

		val result = service.getFile(fileName)

		Assert.assertTrue(Arrays.areEqual(byteArray, result))
	}

	private val byteArray = ByteArray(1)
	private val bucket = "bucket"
	private val fileName = "fileName"
}