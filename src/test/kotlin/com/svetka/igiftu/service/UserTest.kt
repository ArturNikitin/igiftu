package com.svetka.igiftu.service

import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.UserRoles
import java.time.LocalDateTime

abstract class UserTest {
	companion object {
		const val email1 = "email@gmail.com"
		const val email2 = "email2@gmail.com"
		const val login1 = "@email"
		const val login2 = "@email2"
		const val password1 = "1234"
	}
	
	fun getUserCreds() = UserCredentials(
		email1,
		password1
	)
	
	fun getUserToSave() = User(
		id = null,
		createdDate = null,
		password = password1,
		email = email1,
		login =  login1,
		role = UserRoles.ROLE_USER,
		isEnabled = true,
		isAccountNonLocked = true
	)
	
	
	fun getUser() = User(
		1L,
		LocalDateTime.now(),
		password1,
		email1,
		login1,
		UserRoles.ROLE_USER,
		isEnabled = true,
		isAccountNonLocked = true
	)
	
	fun getUser2() = User(
		2L,
		LocalDateTime.now(),
		password1,
		email1,
		login1,
		UserRoles.ROLE_USER,
		isEnabled = true,
		isAccountNonLocked = true
	)
	
	fun getUserDto1() = UserDto(
		1L,
		email =  email1,
		login =  login1
	)
	
	fun getUserDto2() = UserDto(
		1L,
		email =  email2,
		login =  login2
	)
	
	fun getUserDtoToUpdate() = UserDto(
		id = 1L,
		email = email1,
		login =  login2,
	)
	
	fun getUserAfterUpdate() = User(
		1L,
		LocalDateTime.now(),
		password1,
		email1,
		login2,
		UserRoles.ROLE_USER,
		isEnabled = true,
		isAccountNonLocked = true
	)
}