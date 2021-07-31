package com.svetka.igiftu.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.svetka.igiftu.entity.enums.Access

@JsonInclude(JsonInclude.Include.NON_NULL)
data class WishDto (
	var id: Long? = null,
	var name: String,
	var price: Double? = null,
	var access: String = Access.PUBLIC.name,
	var createdDate: String? = null,
	var lastModifiedDate: String? = null,
	var isBooked: Boolean = false,
	var isCompleted: Boolean = false,
	val isAnalogPossible: Boolean = true
) : Content() {
	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as WishDto

		if (id != other.id) return false
		if (name != other.name) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id?.hashCode() ?: 0
		result = 31 * result + name.hashCode()
		return result
	}
}
