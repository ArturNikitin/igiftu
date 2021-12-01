package com.svetka.igiftu.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.stream.Collectors
import javax.crypto.SecretKey
import javax.persistence.EntityNotFoundException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtTokenVerifier(
	private val jwtConfig: JwtConfig,
	private val secretKey: SecretKey
) : OncePerRequestFilter() {
	
	@Suppress("UNCHECKED_CAST")
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain
	) {
		val authorizationHeader: String? = request.getHeader(jwtConfig.getAuthorizationHeader())

		if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer")) {
			filterChain.doFilter(request, response)
		} else {
			val token: String = authorizationHeader.replace("Bearer ", "")
			try {
				val claimsJws: Jws<Claims> = Jwts.parserBuilder()
					.setSigningKey(secretKey)
					.build().parseClaimsJws(token)
				val username: String = claimsJws.body.subject
				val authorities: List<Map<String, String>>? =
					claimsJws.body["authorities"] as List<Map<String, String>>?
				val authoritySet: Set<SimpleGrantedAuthority> = authorities!!.stream()
					.map { auth: Map<String, String> -> SimpleGrantedAuthority(auth["authority"]) }
					.collect(Collectors.toSet())
				val authentication: Authentication = UsernamePasswordAuthenticationToken(
					username,
					null,
					authoritySet
				)
				SecurityContextHolder.getContext().authentication = authentication
				filterChain.doFilter(request, response)
			} catch (e: JwtException) {
				throw IllegalStateException("Token cannot be trusted")
			}
		}
	}
}