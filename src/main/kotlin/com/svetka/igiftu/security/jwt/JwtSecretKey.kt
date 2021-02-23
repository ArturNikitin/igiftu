package com.svetka.igiftu.security.jwt

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.jsonwebtoken.security.Keys
import javax.crypto.SecretKey

@Configuration
class JwtSecretKey{
    private lateinit var jwtConfig: JwtConfig

    @Bean
    fun secretKey(): SecretKey = Keys.hmacShaKeyFor(jwtConfig.key.toByteArray())
}