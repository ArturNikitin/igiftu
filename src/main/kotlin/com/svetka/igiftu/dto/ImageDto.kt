package com.svetka.igiftu.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ImageDto(
	val id: Long? = null,
	var name: String? = null,
	var content: ByteArray? = null
) {
	companion object {
		fun fill(name: String) = ImageDto(null, name, null)

		fun fill(id: Long, name: String) = ImageDto(id, name, null)

		fun fill(content: ByteArray) = ImageDto(null, null, content)

		fun fill(id: Long, name: String, content: ByteArray) = ImageDto(id, name, content)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as ImageDto

		if (id != other.id) return false
		if (name != other.name) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id?.hashCode() ?: 0
		result = 31 * result + (name?.hashCode() ?: 0)
		return result
	}

	override fun toString(): String {
		return "ImageDto(id=$id, name=$name)"
	}
}
