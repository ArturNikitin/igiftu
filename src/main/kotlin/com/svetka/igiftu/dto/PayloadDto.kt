package com.svetka.igiftu.dto

data class PayloadDto(
	val isOwner: Boolean,
	val content: List<Content> = emptyList()
)

open class Content()