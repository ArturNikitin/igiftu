package com.svetka.igiftu.service.manager.impl

import com.svetka.igiftu.component.user.UserComponent
import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.service.manager.ReaderManager
import com.svetka.igiftu.service.manager.SecurityManager
import org.springframework.stereotype.Service

@Service
class ReaderManagerImpl(
	private val securityManager: SecurityManager,
	private val userService: UserComponent,
	private val boardComponent: UserComponent
) : ReaderManager {
	override fun readWishesByUser(userId: Long, readerUsername: String?): PayloadDto {
		return PayloadDto(
			readerUsername?.let { userService.isSameUser(userId, it) } ?: false,
			userService.getWishes(userId)
		)
	}

	override fun readWishesByBoard(boardId: Long, readerUsername: String?): PayloadDto {
		TODO("Not yet implemented")
	}

	override fun readBoardsByUser(userId: Long, readerUsername: String?): PayloadDto {
		return PayloadDto(
			readerUsername?.let { userService.isSameUser(userId, it) } ?: false,
			userService.getBoards(userId)
		)
	}

	override fun readWishesByBoard(boardId: Long, userId: Long, readerUsername: String?): PayloadDto {
		TODO("Not yet implemented")
	}
}