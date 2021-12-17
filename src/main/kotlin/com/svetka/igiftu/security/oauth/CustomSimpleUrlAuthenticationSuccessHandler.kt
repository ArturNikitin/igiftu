package com.svetka.igiftu.security.oauth

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class CustomSimpleUrlAuthenticationSuccessHandler: SimpleUrlAuthenticationSuccessHandler() {
	override fun onAuthenticationSuccess(
		request: HttpServletRequest,
		response: HttpServletResponse,
		authentication: Authentication?
	) {
		redirectStrategy.sendRedirect(request, response, "http://localhost:8081/signUp")
	}
}