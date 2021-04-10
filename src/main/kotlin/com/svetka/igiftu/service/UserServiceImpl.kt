package com.svetka.igiftu.service

import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.repository.UserRepository
import ma.glasnost.orika.MapperFacade
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException

@Service
class UserServiceImpl(
	private val userRepo: UserRepository,
	private val mapper: MapperFacade,
	private val encoder: PasswordEncoder
) : UserService {
	
	@Transactional
	override fun getUserById(id: Long): UserDto =
		userRepo.findById(id).map {
			getUserDto(it)
		}.orElseThrow { EntityNotFoundException() }
	
	@Transactional
	override fun updateUser(userDto: UserDto): UserDto =
		userRepo.findById(userDto.id).map {
			saveOrUpdateUser(it.apply {
				login = userDto.login
			})
		}.orElseThrow { EntityNotFoundException() }
	
	
	@Transactional
	override fun registerUser(userCredentials: UserCredentials): UserDto {
		if (userRepo.getUserByEmail(userCredentials.email).isEmpty) {
			
			val mappedUser = mapper.map(userCredentials, User::class.java).apply {
				createdDate = LocalDateTime.now()
				password = encoder.encode(this.password)
				login = login ?: getLoginFromEmail()
				role = UserRoles.ROLE_USER
			}
			
			return saveOrUpdateUser(mappedUser)
		} else {
			throw EntityExistsException()
		}
	}
	
	private fun saveOrUpdateUser(user: User) = getUserDto(userRepo.save(user))
	
	private fun getUserDto(user: User) = mapper.map(user, UserDto::class.java)
	
	private fun User.getLoginFromEmail() = "@" + email
		.replaceAfter("@", "")
		.removeSuffix("@")
}