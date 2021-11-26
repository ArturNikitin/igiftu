package com.svetka.igiftu.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.svetka.igiftu.entity.enums.Access
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class WishDto (
	@field:NotNull(message = "Поле Id должно пресутствовать в запросе")
	var id: Long? = null,
	@field:NotEmpty(message = "Поле name должно пресутствовать в запросе")
	var name: String = "",
	@field:NotNull(message = "Поле price должно пресутствовать в запросе")
	var price: Double? = null,
	@field:NotNull(message = "Поле access должно пресутствовать в запросе")
	var access: String = Access.PUBLIC.name,
	var createdDate: String? = null,
	var lastModifiedDate: String? = null,
	@field:NotNull(message = "Поле isBooked должно пресутствовать в запросе")
	var isBooked: Boolean = false,
	@field:NotNull(message = "Поле isCompleted должно пресутствовать в запросе")
	var isCompleted: Boolean = false,
	@field:NotNull(message = "Поле isAnalogPossible должно пресутствовать в запросе")
	val isAnalogPossible: Boolean = true,
	val image: ImageDto? = null
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
