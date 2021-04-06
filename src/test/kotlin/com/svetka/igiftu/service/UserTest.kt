package com.svetka.igiftu.service

import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.UserRoles
import java.time.LocalDateTime

abstract class UserTest {
	fun getUserToSaveFirst() = User(
		id = null,
		createdDate = null,
		password = "1234",
		email = "email@gmail.com",
		login =  null,
		role = null,
		isEnabled = true,
		isAccountNonLocked = true
	)
	fun getUserToSave() = User(
		id = null,
		createdDate = null,
		password = "1234",
		email = "email@gmail.com",
		login =  "login",
		role = UserRoles.ROLE_USER,
		isEnabled = true,
		isAccountNonLocked = true
	)
	
	
	fun getUser() = User(
		1L,
		LocalDateTime.now(),
		"1234",
		"email@gmail.com",
		"login",
		UserRoles.ROLE_USER,
		isEnabled = true,
		isAccountNonLocked = true
	)
	
	fun getUser2() = User(
		2L,
		LocalDateTime.now(),
		"1234",
		"email2@gmail.com",
		"login2",
		UserRoles.ROLE_USER,
		isEnabled = true,
		isAccountNonLocked = true
	)
	
	fun getUserDto1() = UserDto(
		1L,
		email =  "email@gmail.com",
		login =  "login"
	)
	
	fun getUserDto2() = UserDto(
		1L,
		email =  "email2@gmail.com",
		login =  "login2"
	)
	
	fun getUserDtoToSave() = UserDto(
		email =  "email2@gmail.com",
		password = "1234"
	)
}