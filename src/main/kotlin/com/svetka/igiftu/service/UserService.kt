package com.svetka.igiftu.service

import com.svetka.igiftu.dto.PasswordDto
import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.dto.WishDto
import org.springframework.stereotype.Service

interface UserService {
    fun getUserById(id: Long) : UserDto
    fun updateUser(userDto: UserDto) : UserDto
    fun registerUser(userCredentials: UserCredentials) : UserDto
    fun getAllWishesByUserId(userId: Long) : PayloadDto
	fun createWish(userId: Long, createWishDto: WishDto) : WishDto
    fun resetPassword(email: String)
    fun updatePassword(password: PasswordDto)
}