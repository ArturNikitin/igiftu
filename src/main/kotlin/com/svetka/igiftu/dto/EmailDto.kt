package com.svetka.igiftu.dto

import javax.validation.constraints.Email

data class EmailDto(
    @field:Email(message = "Неккоректный имейл")
    val email: String
)
