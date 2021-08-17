package com.svetka.igiftu.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsConfig {

	@Value("\${aws.cred.key}")
	private lateinit var awsKey: String

	@Value("\${aws.cred.pass}")
	private lateinit var awsPass: String

	@Bean("awsCredentials")
	fun getAwsCredentials() = BasicAWSCredentials(awsKey, awsPass)

	@Bean
	fun getS3Client(): AmazonS3 = AmazonS3ClientBuilder
		.standard()
		.withCredentials(
			AWSStaticCredentialsProvider(
				getAwsCredentials()
			)
		)
		.withRegion(Regions.EU_NORTH_1)
		.build()

}

