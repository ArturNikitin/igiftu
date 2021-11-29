package com.svetka.igiftu.dto

import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UpdateBoardDto(
	@field:NotNull(message = "Поле Id должно пресутствовать в запросе")
	val id: Long? = null,
	@field:NotEmpty(message = "Поле name должно пресутствовать в запросе")
	val name: String = "",
	val image: ImageDto? = null
)