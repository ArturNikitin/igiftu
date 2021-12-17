package com.svetka.igiftu.security.service

import com.svetka.igiftu.component.user.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
	private val userRepository: UserRepository
) : UserDetailsService {

	override fun loadUserByUsername(email: String): UserDetails {
		val user = userRepository.findUserByEmail(email).orElseThrow {
			UsernameNotFoundException("User with email $email not found")
		}
//		TODO change error to show better message
		if(user.provider != "LOCAL") throw SecurityException("Try to sigh in with ${user.provider}")

//		todo in case of FACEBOOK
		return MyUserDetails(user.email, user.password!!, user.role.toString())
	}
}