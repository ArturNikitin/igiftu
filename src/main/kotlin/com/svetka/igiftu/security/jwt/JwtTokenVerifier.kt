package com.svetka.igiftu.security.jwt

import io.jsonwebtoken.JwtException
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenVerifier(
	private val tokenProvider: TokenProvider
) : OncePerRequestFilter() {

	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		val authorizationHeader: String? = request.getHeader(AUTHORIZATION)

		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
			filterChain.doFilter(request, response)
		} else {
			val token: String = authorizationHeader.replace("Bearer ", "")
			try {
				val username: String = tokenProvider.getUserEmailFromToken(token)
				val authoritySet = tokenProvider.getUserRolesFromToken(token)
				val authentication: Authentication = UsernamePasswordAuthenticationToken(
					username,
					null,
					authoritySet
				)
				SecurityContextHolder.getContext().authentication = authentication
				filterChain.doFilter(request, response)
			} catch (e: JwtException) {
				throw IllegalStateException(String.format("Token {%s} cannot be trusted", token))
			}
		}
	}
}