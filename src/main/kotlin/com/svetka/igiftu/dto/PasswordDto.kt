package com.svetka.igiftu.dto

import org.hibernate.validator.constraints.Length

data class PasswordDto(
    @field:Length(min = 6, max = 25, message = "Пароль должен быть от 6 до 25 символов")
    val password: String,
    val token: String
)
