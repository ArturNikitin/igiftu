package com.svetka.igiftu.service

import com.svetka.igiftu.dto.ImageDto
import com.svetka.igiftu.entity.Image

interface ImageService {
	fun uploadImage(content: ByteArray): ImageDto
	fun getDefaultImage(imageName: String): ImageDto
	fun getContent(imageName: String): ByteArray
	fun deleteImageIfExists(imageName: String?)
}