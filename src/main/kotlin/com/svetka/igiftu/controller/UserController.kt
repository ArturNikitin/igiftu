package com.svetka.igiftu.controller


import com.svetka.igiftu.entity.User
import com.svetka.igiftu.repository.UserRepository
import com.svetka.igiftu.service.UserDto
import com.svetka.igiftu.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/user")
class UserController(
	private val userRepository: UserRepository,
	private val userService: UserService
) {

	@GetMapping("/{id}")
	fun getUser(
		@PathVariable id: Long
	): User {
		val userById = userRepository.getUserById(id)
		return userById!!
	}

	@PostMapping("/registration")
	fun createUser(@RequestBody user: UserDto) = userService.createUser(user)

	@GetMapping("/get")
	fun getSignedInUser(user: Principal) = user

	@GetMapping("/role/admin")
	@PreAuthorize("hasRole('ADMIN')")
	fun checkRoleAdmin() = "true"

	@GetMapping("/role/user")
	@PreAuthorize("hasRole('USER')")
	fun checkRoleUser() = "true"


}