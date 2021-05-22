package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.repository.UserRepository
import com.svetka.igiftu.service.EmailService
import com.svetka.igiftu.service.UserService
import com.svetka.igiftu.service.WishService
import ma.glasnost.orika.MapperFacade
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException

@Service
class UserServiceImpl(
	private val userRepo: UserRepository,
	private val mapper: MapperFacade,
	private val encoder: PasswordEncoder,
	private val emailService: EmailService,
	private val wishService: WishService
) : UserService {
	
	@Transactional
	override fun getUserById(id: Long): UserDto =
		userRepo.findById(id).map {
			getUserDto(it)
		}.orElseThrow { EntityNotFoundException("User with id $id not found ") }
	
	@Transactional
	override fun updateUser(userDto: UserDto): UserDto =
		userRepo.findById(userDto.id).map {
			saveOrUpdateUser(it.apply {
				login = userDto.login
			})
		}.orElseThrow { EntityNotFoundException("User with id ${userDto.id} not found ") }
	
	
	@Transactional
	override fun registerUser(userCredentials: UserCredentials): UserDto {
		if (userRepo.getUserByEmail(userCredentials.email).isEmpty) {
			
			val mappedUser = mapper.map(userCredentials, User::class.java).apply {
				createdDate = LocalDateTime.now()
				password = encoder.encode(this.password)
				login = login ?: getLoginFromEmail()
				role = UserRoles.ROLE_USER
			}
			CompletableFuture.supplyAsync { emailService.sendEmail(mappedUser.email) }
			return saveOrUpdateUser(mappedUser)
		} else {
			throw EntityExistsException()
		}
	}
	
	@Transactional
	override fun getAllWishesByUserId(userId: Long): PayloadDto {
		checkConditions(userId)
		return PayloadDto(isOwner(userId), wishService.getWishesByUserId(userId))
	}
	
	@Transactional
	override fun createWish(userId: Long, createWishDto: WishDto): WishDto {
		checkConditions(userId)
		return WishDto(name = "test")
	}
	
	private fun checkConditions(userId: Long) {
		if (!userRepo.existsById(userId)) throw EntityNotFoundException("User with id $userId not found")
	}
	//TODO add logic to this method
	private fun isOwner(userId: Long) = userId > 0 
	
	private fun saveOrUpdateUser(user: User) = getUserDto(userRepo.save(user))
	
	private fun getUserDto(user: User) = mapper.map(user, UserDto::class.java)
	
	private fun User.getLoginFromEmail() = "@" + email
		.replaceAfter("@", "")
		.removeSuffix("@")
}