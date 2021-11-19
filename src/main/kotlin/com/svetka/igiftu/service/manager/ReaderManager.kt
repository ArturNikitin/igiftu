package com.svetka.igiftu.service.manager

import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.dto.RequestDto
import com.svetka.igiftu.dto.UserInfo

interface ReaderManager {
	fun readWishesByUser(userId: Long, readerUsername: String?): PayloadDto
	fun readWishesByBoard(boardId: Long, readerUsername: String?): PayloadDto
	fun readBoardsByUser(userId: Long, readerUsername: String?): PayloadDto
	fun readWishesByBoard(boardId: Long, userId: Long, readerUsername: String?): PayloadDto
}