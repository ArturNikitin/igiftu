package com.svetka.igiftu.service.impl

import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.repository.UserRepository
import com.svetka.igiftu.service.UserDto
import com.svetka.igiftu.service.UserService
import ma.glasnost.orika.MapperFacade
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException

@Service
class UserServiceImpl(
	private val userRepository: UserRepository,
	private val mapper: MapperFacade,
	private val encoder: PasswordEncoder
) : UserService {

	@Transactional
	override fun getUserByEmail(email: String): UserDto {
		val userByEmail = userRepository.getUserByEmail(email)
			?: throw EntityNotFoundException("User with email $email not found")
		return getUserDto(userByEmail)
	}

	@Transactional
	override fun createUser(userDto: UserDto): UserDto {
		val user = mapper.map(userDto, User::class.java)
		user.isAccountNonLocked = true
		user.isEnabled = true
		user.createdDate = LocalDateTime.now()
		user.role = UserRoles.ROLE_USER
		user.password = encoder.encode(userDto.password)
		user.login = user.email
			.replaceAfterLast("@", "")

		val user1 = userRepository.save(user)
		val userDto1 = getUserDto(user1)
		return userDto1
	}

	private fun getUserDto(user: User) = mapper.map(user, UserDto::class.java)

}