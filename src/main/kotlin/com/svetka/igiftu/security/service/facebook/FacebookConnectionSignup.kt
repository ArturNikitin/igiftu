package com.svetka.igiftu.security.service.facebook

import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.RegistrationTypes
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.repository.UserRepository
import java.time.LocalDateTime
import org.springframework.social.connect.Connection
import org.springframework.social.connect.ConnectionSignUp
import org.springframework.stereotype.Service

@Service
class FacebookConnectionSignup(
	private val userRepository: UserRepository
) : ConnectionSignUp {
	override fun execute(connection: Connection<*>): String {
		val user = User(
			createdDate = LocalDateTime.now(),
			role = UserRoles.ROLE_USER,
			registrationType = RegistrationTypes.FACEBOOK,
			email = connection.displayName,
			id = null
		)
		return userRepository.save(user).email
	}
}