package com.svetka.igiftu.dto

import com.svetka.igiftu.service.Readable
import com.svetka.igiftu.service.impl.OwnerType

data class RequestDto(
	val ownerId: Long,
	val userId: Long,
	val username: String?,
	val ownerType: OwnerType,
	val content: Content?,
	val service: Readable
)
