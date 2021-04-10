package com.svetka.igiftu.service

import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import org.springframework.stereotype.Service

@Service
interface UserService {
    fun getUserById(id: Long) : UserDto
    fun createUser(userDto: UserDto) : UserDto
    fun registerUser(userCredentials: UserCredentials) : UserDto
}