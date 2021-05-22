package com.svetka.igiftu.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class WishDto (
	val id: Long? = null,
	val name: String,
	val price: Double?,
	val access: String
) : Payload()
