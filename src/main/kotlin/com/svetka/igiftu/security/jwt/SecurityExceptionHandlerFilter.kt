package com.svetka.igiftu.security.jwt

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

class SecurityExceptionHandlerFilter(
	private val resolver: HandlerExceptionResolver
) : OncePerRequestFilter() {
	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		chain: FilterChain
	) {
		try {
			chain.doFilter(request, response)
		} catch (ex: Exception) {
			resolver.resolveException(
				request,
				response, null, ex
			)
		}
	}
}