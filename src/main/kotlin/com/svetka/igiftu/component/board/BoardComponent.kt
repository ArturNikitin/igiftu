package com.svetka.igiftu.component.board

import com.svetka.igiftu.dto.BoardDto
import com.svetka.igiftu.dto.WishDto

interface BoardComponent {
	fun createBoard(boardDto: BoardDto): BoardDto
	fun addWishes(boardId: Long, wishesDto: Set<WishDto>): BoardDto
	fun deleteWishes(boardId: Long, wishesDto: Set<WishDto>): BoardDto
}