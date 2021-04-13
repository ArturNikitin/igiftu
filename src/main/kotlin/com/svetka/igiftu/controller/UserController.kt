package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.service.UserService
import java.util.HashMap
import java.util.function.Consumer
import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
@CrossOrigin
class UserController(
	private val userService: UserService
) {
	
	@GetMapping("{id}")
	fun getUser(@PathVariable id: Long): UserDto {
		return userService.getUserById(id)
	}
	
	@PostMapping
	fun createUser(@RequestBody user: UserDto) = userService.updateUser(user)
	
	@PostMapping("/registration")
	@ResponseStatus(HttpStatus.CREATED)
	fun registerUser(@Valid @RequestBody user: UserCredentials) = userService.registerUser(user)
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handleValidationExceptions(
		ex: MethodArgumentNotValidException
	): Map<String, String?> {
		val errors: MutableMap<String, String?> = HashMap()
		ex.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
			val fieldName = (error as FieldError).field
			val errorMessage = error.getDefaultMessage()
			errors[fieldName] = errorMessage
		})
		return errors
	}
}