package com.svetka.igiftu.security.service.facebook

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.social.connect.Connection
import org.springframework.social.connect.web.SignInAdapter
import org.springframework.web.context.request.NativeWebRequest

class FacebookSignInAdapter : SignInAdapter {
	override fun signIn(
		localUserId: String?,
		connection: Connection<*>,
		request: NativeWebRequest
	): String? {
		SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
			connection.displayName, null, mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
		)

		return null
	}
}