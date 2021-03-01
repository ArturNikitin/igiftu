package com.svetka.igiftu.security.service

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

class MyUserDetails(
	private val email: String,
	private val password: String,
	private val authority: List<String>
) : UserDetails {


	override fun getAuthorities(): List<SimpleGrantedAuthority> {
		return authority.map { SimpleGrantedAuthority(it) }
	}

	override fun isEnabled(): Boolean {
		return true
	}

	override fun getUsername(): String {
		return email
	}

	override fun isCredentialsNonExpired(): Boolean {
		return true
	}

	override fun getPassword(): String {
		return password
	}

	override fun isAccountNonExpired(): Boolean {
		return true
	}

	override fun isAccountNonLocked(): Boolean {
		return true
	}
}