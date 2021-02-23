package com.svetka.igiftu.service

interface UserService {
	fun getUserByEmail(email: String): UserDto
	fun createUser(userDto: UserDto): UserDto
}

data class UserDto(
	val email: String,
	val password: String,
	val login: String?,
	val id: Long?
)