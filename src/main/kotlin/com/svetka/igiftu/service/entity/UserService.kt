package com.svetka.igiftu.service.entity

import com.svetka.igiftu.dto.PasswordDto
import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.service.Possessing

interface UserService : Possessing {
    fun getUserById(id: Long) : UserDto
    fun update(userDto: UserDto) : UserDto
    fun register(userCredentials: UserCredentials) : UserDto
    fun resetPassword(email: String)
    fun updatePassword(password: PasswordDto)
}