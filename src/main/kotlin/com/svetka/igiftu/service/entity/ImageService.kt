package com.svetka.igiftu.service.entity

import com.svetka.igiftu.dto.ImageDto

interface ImageService {
	fun saveImage(content: ByteArray): ImageDto
	fun getDefaultImage(imageName: String): ImageDto
	fun getContent(imageName: String): ByteArray
}