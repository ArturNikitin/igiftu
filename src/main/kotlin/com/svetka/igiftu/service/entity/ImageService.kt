package com.svetka.igiftu.service.entity

import com.svetka.igiftu.dto.ImageDto

interface ImageService {
	fun saveImage(imageName: String, content: ByteArray): ImageDto
	fun getImage(imageName: String): ImageDto
}