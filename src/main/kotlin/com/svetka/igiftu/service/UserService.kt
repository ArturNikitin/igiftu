package com.svetka.igiftu.service

import com.svetka.igiftu.dto.PasswordDto
import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.User
import org.springframework.stereotype.Service

interface UserService {
    fun getUserById(id: Long) : UserDto
    fun update(userDto: UserDto) : UserDto
    fun register(userCredentials: UserCredentials) : UserDto
    fun getAllWishes(userId: Long) : PayloadDto
    fun resetPassword(email: String)
    fun updatePassword(password: PasswordDto)
    fun get(userId: Long) : User
    fun addWish(userId: Long, wishDto: WishDto): WishDto
}