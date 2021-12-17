package com.svetka.igiftu.security.oauth

import com.svetka.igiftu.security.jwt.EXPIRATION_TIME
import com.svetka.igiftu.security.jwt.JwtConfig
import com.svetka.igiftu.security.jwt.PREFIX
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.util.Date
import javax.crypto.SecretKey
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.MediaType
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter

class OAuthHandleResponseFilter(
	private val secretKey: SecretKey,
	private val jwtConfig: JwtConfig
) : OncePerRequestFilter() {
	private val log = KotlinLogging.logger { }
	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
		log.debug { "${this::class.java.name} working" }
		try {
			val user = SecurityContextHolder.getContext().authentication as OAuth2AuthenticationToken
			val email = user.principal.getAttribute<String>("email")
				?: throw IllegalArgumentException("OAuth user must have an email")
			val provider = user.authorizedClientRegistrationId
			log.info { "Found signIn user from $provider" }
			val token = Jwts.builder()
				.setSubject(email)
				.claim("authorities", listOf(mapOf("authority" to "ROLE_USER")))
				.setIssuedAt(Date())
				.setExpiration(Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(Keys.hmacShaKeyFor(jwtConfig.key.toByteArray()))
				.compact()

			response.status = (HttpServletResponse.SC_OK)
			response.contentType = MediaType.APPLICATION_JSON_VALUE
			response.characterEncoding = StandardCharsets.UTF_8.toString()
			response.addHeader(
				jwtConfig.getAuthorizationHeader(),
				"$PREFIX $token"
			)
			response.addHeader("X-Provider", provider)
		} catch (ignore: ClassCastException) {

		} catch (ex: Exception) {
			log.error { ex.message }
		}
		chain.doFilter(request, response)
	}
}

data class UserProviderCredentials(
	val email: String,
	val provider: String
)