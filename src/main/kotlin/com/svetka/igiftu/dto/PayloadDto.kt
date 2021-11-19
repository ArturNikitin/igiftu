package com.svetka.igiftu.dto

data class PayloadDto(
	val isOwner: Boolean,
	val content: Set<Content> = emptySet()
)

open class Content()