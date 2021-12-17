package com.svetka.igiftu.controller

import com.svetka.igiftu.component.user.UserComponent
import com.svetka.igiftu.security.oauth.UserProviderCredentials
import javax.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/signUp")
class AuthController(
	private val userComponent: UserComponent
) {
	private val logger = KotlinLogging.logger { }

	@Value("\${redirect.front}")
	private lateinit var redirectUrl: String

	@GetMapping
	fun test(
		oAuthUser: OAuth2AuthenticationToken,
		response: HttpServletResponse
	) {
		logger.info { "Received request to sign in with {${oAuthUser.authorizedClientRegistrationId}}" }
		val email = oAuthUser.principal.attributes["email"] ?: throw IllegalArgumentException("OAuth user must have an email")
		val token = response.getHeader(HttpHeaders.AUTHORIZATION)
		val provider = response.getHeader("X-Provider")
		val user = userComponent.processOAuth2SignInUser(UserProviderCredentials(email as String, provider))
		logger.info { "Process user with id ${user.id}" }
		return response.sendRedirect("$redirectUrl?id=${user.id}&token=$token")
	}
}