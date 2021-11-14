package com.svetka.igiftu.dto

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BoardDto(
	val id: Long? = null,
	var name: String,
	var createdDate: String? = null,
	var lastModifiedDate: String? = null,
	var image: ImageDto? = null,
	var wishes: Set<WishDto> = emptySet()
) : Content()
