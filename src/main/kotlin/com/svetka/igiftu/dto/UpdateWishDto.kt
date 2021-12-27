package com.svetka.igiftu.dto

import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UpdateWishDto(
	@field:NotNull(message = "Поле Id должно присутствовать в запросе")
	val id: Long? = null,
	@field:NotEmpty(message = "Поле name должно присутствовать в запросе")
	val name: String = "",
	@field:NotNull(message = "Поле access должно присутствовать в запросе")
	val access: String? = null,
	@field:NotNull(message = "Поле isBooked должно присутствовать в запросе")
	val isBooked: Boolean? = null,
	@field:NotNull(message = "Поле isCompleted должно присутствовать в запросе")
	val isCompleted: Boolean? = null,
	@field:NotNull(message = "Поле isAnalogPossible должно присутствовать в запросе")
	val isAnalogPossible: Boolean? = null,
	val image: ImageDto? = null,
	@field:NotNull(message = "поле price должно присутствовать в запросе")
	val price: priceDto? = null,
	@field:NotNull(message = "поле location должно присутствовать в запросе")
	val location: String? = null,
	@field:NotNull(message = "поле link должно присутствовать в запросе")
	val link: String? = null,
	@field:NotNull(message = "поле details должно присутствовать в запросе")
	val details: String? = null,
	@field:NotNull(message = "поле buyingMyself должно пристутствовать в запросу")
	val buyingMyself: Boolean? = null
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

data class priceDto(
	val value: Double? = null,
	val currencyCode: Int? = null
)