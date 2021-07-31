package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.service.ContentReaderManager
import com.svetka.igiftu.service.SecurityManager
import com.svetka.igiftu.service.UserService
import com.svetka.igiftu.service.impl.ContentType.BOARD
import com.svetka.igiftu.service.impl.ContentType.WISH
import org.springframework.stereotype.Service

@Service
class ContentReaderManagerImpl(
	private val userService: UserService,
	private val securityManager: SecurityManager
) : ContentReaderManager {

	override fun getContent(userId: Long, username: String?, type: ContentType): PayloadDto {
		return PayloadDto(
			securityManager.isOwner(userId, username),
			when (type) {
				WISH -> userService.getAllWishes(userId)
				BOARD -> TODO()
			}
		)
	}
}