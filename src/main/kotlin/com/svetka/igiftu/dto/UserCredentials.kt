package com.svetka.igiftu.dto

import javax.validation.constraints.Email
import org.hibernate.validator.constraints.Length

data class UserCredentials(
	@Email(message = "Неккоректный имейл")
	val email: String,
	@Length(min = 6, max = 25, message = "Пароль должен быть от 6 до 25 символов")
	val password: String
)
