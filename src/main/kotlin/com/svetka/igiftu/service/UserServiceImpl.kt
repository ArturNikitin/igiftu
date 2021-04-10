package com.svetka.igiftu.service

import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.repository.UserRepository
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException
import ma.glasnost.orika.MapperFacade
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
	val userRepo: UserRepository,
	val mapper: MapperFacade,
	val encoder: PasswordEncoder
) : UserService {
	
	@Transactional
	override fun getUserById(id: Long): UserDto {
		val userById = userRepo.getUserById(id) ?: throw EntityNotFoundException()
		
		return getUserDto(userById)
	}
	
	@Transactional
	override fun createUser(userDto: UserDto): UserDto {
		val mappedUser = mapper.map(userDto, User::class.java).apply {
			createdDate = LocalDateTime.now()
			password = userDto.password.let { encoder.encode(this.password) }
			login = login ?: getLoginFromEmail()
			role = UserRoles.ROLE_USER
		}
		
		val savedUser = userRepo.save(mappedUser)
		
		return getUserDto(savedUser)
	}
	
	override fun registerUser(userCredentials: UserCredentials): UserDto {
		val mappedUser = mapper.map(userCredentials, User::class.java).apply {
			createdDate = LocalDateTime.now()
			password = encoder.encode(this.password)
			login = login ?: getLoginFromEmail()
			role = UserRoles.ROLE_USER
		}
		
		return getUserDto(userRepo.save(mappedUser))
	}
	
	private fun getUserDto(user: User) = mapper.map(user, UserDto::class.java)
 
	private fun User.getLoginFromEmail() = "@" + email
        .replaceAfter("@", "")
        .removeSuffix("@")
}