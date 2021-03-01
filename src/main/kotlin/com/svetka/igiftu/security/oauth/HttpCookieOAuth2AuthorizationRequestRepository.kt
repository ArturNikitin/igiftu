package com.svetka.igiftu.security.oauth

import com.svetka.igiftu.security.oauth.CookieUtils.addCookie
import com.svetka.igiftu.security.oauth.CookieUtils.deleteCookie
import com.svetka.igiftu.security.oauth.CookieUtils.serialize
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class HttpCookieOAuth2AuthorizationRequestRepository : AuthorizationRequestRepository<OAuth2AuthorizationRequest> {
    private val oAuth2Cookie = "oauth2_auth_request"
    private val redirectUriOAuth2Cookie = "redirect_uri"
    private val cookieExpireSeconds = 180

    override fun loadAuthorizationRequest(request: HttpServletRequest): OAuth2AuthorizationRequest {
        return CookieUtils.getCookie(request, oAuth2Cookie)
            .map { cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest::class.java) }
            .orElse(null)
    }

    override fun saveAuthorizationRequest(
        authorizationRequest: OAuth2AuthorizationRequest?,
        request: HttpServletRequest,
        response: HttpServletResponse?
    ) {
        if (authorizationRequest == null) {
            deleteCookie(request, response!!, oAuth2Cookie)
            deleteCookie(request, response, redirectUriOAuth2Cookie)
            return
        }
        addCookie(
            response!!,
            oAuth2Cookie,
            serialize(authorizationRequest),
            cookieExpireSeconds
        )
        val redirectUriAfterLogin = request.getParameter(redirectUriOAuth2Cookie)
        if (redirectUriAfterLogin.isNotBlank()) {
            addCookie(response, redirectUriOAuth2Cookie, redirectUriAfterLogin, cookieExpireSeconds)
        }
    }

    override fun removeAuthorizationRequest(request: HttpServletRequest?): OAuth2AuthorizationRequest? {
        return loadAuthorizationRequest(request!!)
    }

    fun removeAuthorizationRequestCookies(request: HttpServletRequest?, response: HttpServletResponse?) {
        deleteCookie(request!!, response!!, oAuth2Cookie)
        deleteCookie(request, response, redirectUriOAuth2Cookie)
    }
}