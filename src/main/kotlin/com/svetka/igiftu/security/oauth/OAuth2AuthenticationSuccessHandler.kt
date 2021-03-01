package com.svetka.igiftu.security.oauth

import com.svetka.igiftu.config.AppProperties
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class OAuth2AuthenticationSuccessHandler(
    val properties: AppProperties,
    val httpCookieOAuth2Repository: HttpCookieOAuth2AuthorizationRequestRepository
): SimpleUrlAuthenticationSuccessHandler() {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        super.onAuthenticationSuccess(request, response, authentication)
    }
}