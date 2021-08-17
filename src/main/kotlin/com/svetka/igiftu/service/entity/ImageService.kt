package com.svetka.igiftu.service.entity

import com.svetka.igiftu.dto.ImageDto

interface ImageService {
	fun saveImage(imageDto: ImageDto): ImageDto
	fun getImage(imageName: String): ImageDto
}