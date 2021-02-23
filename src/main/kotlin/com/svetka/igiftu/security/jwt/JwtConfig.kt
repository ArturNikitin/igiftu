package com.svetka.igiftu.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "application.jwt")
class JwtConfig{
    val key: String = ""

    fun getAuthorizationHeader() = HttpHeaders.AUTHORIZATION
}