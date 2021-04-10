package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.service.UserService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
@CrossOrigin
class UserController(
	val userService: UserService
) {
	
	@GetMapping("{id}")
	fun getUser(@PathVariable id: Long): UserDto {
		return userService.getUserById(id)
	}
	
	@PostMapping
	fun createUser(@RequestBody user: UserDto) = userService.updateUser(user)
	
	@PostMapping("/registration")
	fun registerUser(@RequestBody user: UserCredentials) = userService.registerUser(user)
}