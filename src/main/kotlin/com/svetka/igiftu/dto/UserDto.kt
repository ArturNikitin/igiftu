package com.svetka.igiftu.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserDto(
    val id: Long = 0L,
    val email: String = "",
    val login: String? = null,
    val role: String? = null,
    val image: ImageDto? = null,
    var wishAmount: Int? = null,
    var boardAmount: Int? = null
)