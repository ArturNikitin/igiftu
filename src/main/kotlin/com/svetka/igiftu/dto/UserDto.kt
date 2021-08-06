package com.svetka.igiftu.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserDto(
    @JsonProperty
    val id: Long = 0L,
    @JsonProperty
    val email: String = "",
    @JsonProperty
    val login: String? = null
)