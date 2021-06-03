package com.svetka.igiftu.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.svetka.igiftu.entity.enums.Access

@JsonInclude(JsonInclude.Include.NON_NULL)
data class WishDto (
	val id: Long? = null,
	val name: String,
	val price: Double? = null,
	val access: String = Access.PUBLIC.name
) : Payload()
