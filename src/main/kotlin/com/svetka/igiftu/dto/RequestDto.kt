package com.svetka.igiftu.dto

import com.svetka.igiftu.service.Readable
import com.svetka.igiftu.service.impl.OwnerType

data class RequestDto(
	val ownerId: Long,
	val userId: Long,
	val username: String?,
	val ownerType: OwnerType,
	val contentId: Long?,
	val content: Content?,
	val service: Readable
)

fun fillUserReadRequest(userId: Long, username: String?, service: Readable) =
	RequestDto(
		userId,
		userId,
		username,
		OwnerType.USER,
		null,
		null,
		service
	)
