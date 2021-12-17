package com.svetka.igiftu.security.oauth

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomSimpleUrlAuthenticationSuccessHandler: SimpleUrlAuthenticationSuccessHandler() {

	@Value("\${redirect.backend}")
	private lateinit var signUpUrl: String

	override fun onAuthenticationSuccess(
		request: HttpServletRequest,
		response: HttpServletResponse,
		authentication: Authentication?
	) {
		redirectStrategy.sendRedirect(request, response, signUpUrl)
	}
}