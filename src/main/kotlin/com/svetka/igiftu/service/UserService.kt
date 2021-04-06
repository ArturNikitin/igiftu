package com.svetka.igiftu.service

import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.entity.User
import org.springframework.stereotype.Service

@Service
interface UserService {
    fun getUserById(id: Long) : UserDto
    fun createUser(userDto: UserDto) : UserDto
}