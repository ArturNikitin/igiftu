package com.svetka.igiftu.dto

import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UpdateWishDto(
	@field:NotNull(message = "Поле Id должно пресутствовать в запросе")
	val id: Long? = null,
	@field:NotEmpty(message = "Поле name должно пресутствовать в запросе")
	val name: String = "",
	@field:NotNull(message = "Поле price должно пресутствовать в запросе")
	val price: Double? = null,
	@field:NotNull(message = "Поле access должно пресутствовать в запросе")
	val access: String? = null,
	@field:NotNull(message = "Поле isBooked должно пресутствовать в запросе")
	val isBooked: Boolean? = null,
	@field:NotNull(message = "Поле isCompleted должно пресутствовать в запросе")
	val isCompleted: Boolean? = null,
	@field:NotNull(message = "Поле isAnalogPossible должно пресутствовать в запросе")
	val isAnalogPossible: Boolean? = null,
	val image: ImageDto? = null
) {
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