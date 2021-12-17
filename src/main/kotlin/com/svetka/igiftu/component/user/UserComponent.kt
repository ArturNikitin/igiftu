package com.svetka.igiftu.component.user

import com.svetka.igiftu.dto.BoardDto
import com.svetka.igiftu.dto.PasswordDto
import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Board
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.security.oauth.UserProviderCredentials

interface UserComponent {
	fun isSameUser(userId: Long, username: String): Boolean
	fun getUserById(id: Long): UserDto
	fun update(userDto: UserDto, username: String): UserDto
	fun register(userCredentials: UserCredentials): UserDto
	fun processOAuth2SignInUser(userProviderCredentials: UserProviderCredentials): UserDto
	fun requestResetPassword(email: String)
	fun updatePassword(password: PasswordDto)
	fun addWishes(userId: Long, wishes: Set<Wish>): Set<WishDto>
	fun addBoards(userId: Long, boards: Set<Board>): Set<BoardDto>
	fun deleteBoards(userId: Long, boards: Set<WishDto>)
	fun getWishes(userId: Long): Set<WishDto>
	fun getBoards(userId: Long): Set<BoardDto>
}