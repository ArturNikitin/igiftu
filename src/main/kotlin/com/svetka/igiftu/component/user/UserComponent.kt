package com.svetka.igiftu.component.user

import com.svetka.igiftu.dto.BoardDto
import com.svetka.igiftu.dto.PasswordDto
import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Board
import com.svetka.igiftu.entity.Wish

interface UserComponent {
	fun getUserById(id: Long) : UserDto
	fun update(userDto: UserDto, username: String) : UserDto
	fun register(userCredentials: UserCredentials) : UserDto
	fun requestResetPassword(email: String)
	fun updatePassword(password: PasswordDto)
	fun addWishes(userId: Long, wishes: Set<Wish>): Set<WishDto>
	fun deleteWishes(userId: Long, wishes: Set<Wish>): Set<WishDto>
	fun addBoards(userId: Long, boards: Set<Board>): Set<BoardDto>
	fun deleteBoards(userId: Long, boards: Set<WishDto>)
}