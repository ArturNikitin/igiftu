package com.svetka.igiftu.security.jwt

import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component

@Component
class JwtConfig {
	var key: String = "SecretKeySecretKeySecretKeySecretKeySecretKeySecretKeySecretKey" +
			"SecretKeySecretKeySecretKeySecretKeySecretKeySecretKeySecretKeySecretKey" +
			"SecretKeySecretKeySecretKeySecretKeySecretKeySecretKeySecretKeySecretKey"

	fun getAuthorizationHeader() = HttpHeaders.AUTHORIZATION
}