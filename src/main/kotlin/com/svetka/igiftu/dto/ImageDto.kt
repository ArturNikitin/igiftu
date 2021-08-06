package com.svetka.igiftu.dto

data class ImageDto(
	val id: Long?,
	val name: String?,
	val content: String?
) {
	companion object {
		fun fill(name: String) = ImageDto(null, name, null)

		fun fill(content: ByteArray) = ImageDto(null, null, content.contentToString())

		fun fill(name: String, content: ByteArray) = ImageDto(null, name, content.contentToString())
	}

}
