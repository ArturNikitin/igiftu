package com.svetka.igiftu.dto

data class PayloadDto(
	val isOwner: Boolean,
	val payload: List<Payload>? = null
)

open class Payload