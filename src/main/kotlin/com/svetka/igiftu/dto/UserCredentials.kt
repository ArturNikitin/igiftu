package com.svetka.igiftu.dto

import javax.validation.constraints.Email
import org.hibernate.validator.constraints.Length

data class UserCredentials(
	@field:Email(message = "Неккоректный имейл")
	val email: String,
	@field:Length(min = 6, max = 25, message = "Пароль должен быть от 6 до 25 символов")
	val password: String
) {
	override fun toString(): String {
		return "UserCredentials(email='$email')"
	}
}
