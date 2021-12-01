package com.svetka.igiftu.controller

import com.svetka.igiftu.component.user.UserComponent
import com.svetka.igiftu.dto.EmailDto
import com.svetka.igiftu.dto.PasswordDto
import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import java.security.Principal
import javax.validation.Valid
import mu.KotlinLogging
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController(
	private val userService: UserComponent
) {
	private val logger = KotlinLogging.logger { }

	@GetMapping("/{userId}")
	fun getUser(@PathVariable userId: Long): UserDto {
		logger.info { "Received request to getUser [$userId]" }
		val user = userService.getUserById(userId)
		logger.info { "Finished request to getUser with data {$user}" }
		return user
	}

	@PostMapping("/login")
	@ResponseStatus(OK)
	fun login(@RequestBody userCreds: UserCredentials) {}

	@PutMapping("/{userId}")
	@ResponseStatus(ACCEPTED)
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	fun updateUser(
		@RequestBody userDto: UserDto,
		@PathVariable userId: String,
		principal: Principal
	): UserDto {
		logger.info { "Received request to updateUser [$userId]" }
		val updatedUser = userService.update(userDto, principal.name)
		logger.info { "Finished request to updateUser with data {$updatedUser}" }
		return updatedUser
	}

	@PostMapping("/registration")
	@ResponseStatus(CREATED)
	fun registerUser(@Valid @RequestBody userCreds: UserCredentials): UserDto {
		logger.info { "Received request to registerUser with creds $userCreds" }
		val userDto = userService.register(userCreds)
		logger.info { "Finished request to registerUser with data {$userDto}" }
		return userDto
	}

	@PostMapping("/password")
	fun resetPassword(@RequestBody email: EmailDto): String {
		userService.requestResetPassword(email.email)
		return "На ваш имейл отправлены ссылка для перехода обновление пароля"
	}

	@PostMapping("/password/update")
	fun updatePassword(
		@RequestBody passwordDto: PasswordDto
	): String {
		userService.updatePassword(passwordDto)
		return "Ваш пароль был успешно изменен"
	}

//	TODO get user by email oor login
}