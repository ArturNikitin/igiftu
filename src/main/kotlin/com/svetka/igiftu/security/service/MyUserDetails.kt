package com.svetka.igiftu.security.service

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class MyUserDetails(
	private val email: String,
	private val password: String,
	private val authority: String
) : UserDetails {


	override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
		return mutableListOf(SimpleGrantedAuthority(authority))
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