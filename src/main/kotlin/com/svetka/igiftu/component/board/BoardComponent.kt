package com.svetka.igiftu.component.board

import com.svetka.igiftu.dto.BoardDto
import com.svetka.igiftu.dto.UpdateBoardDto
import com.svetka.igiftu.dto.UserInfo
import com.svetka.igiftu.dto.WishDto

interface BoardComponent {
	fun createBoard(boardDto: BoardDto, userInfo: UserInfo): BoardDto
	fun addWishes(boardId: Long, wishesDto: Set<WishDto>, userInfo: UserInfo): BoardDto
	fun deleteWishes(boardId: Long, wishesDto: Set<WishDto>, userInfo: UserInfo): BoardDto
	fun deleteBoard(boardId: Long, userInfo: UserInfo)
	fun updateBoard(board: UpdateBoardDto, userInfo: UserInfo): BoardDto
}