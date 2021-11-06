package com.svetka.igiftu.service.entity.impl

import com.svetka.igiftu.dto.Content
import com.svetka.igiftu.dto.PasswordDto
import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Image
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.exceptions.UnknownContentTypeException
import com.svetka.igiftu.repository.UserRepository
import com.svetka.igiftu.service.common.EmailService
import com.svetka.igiftu.service.entity.ImageService
import com.svetka.igiftu.service.entity.TokenService
import com.svetka.igiftu.service.entity.UserService
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException
import ma.glasnost.orika.MapperFacade
import mu.KotlinLogging
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
	private val userRepo: UserRepository,
	private val mapper: MapperFacade,
	private val encoder: PasswordEncoder,
	private val emailService: EmailService,
	private val tokenService: TokenService,
	private val imageService: ImageService
) : UserService {

	private val logger = KotlinLogging.logger { }

	@Transactional
	override fun updatePassword(password: PasswordDto) {
		val user = tokenService.verifyToken(password.token)
		user.password = encoder.encode(password.password)
		userRepo.save(user)
	}

	@Transactional
	override fun getUserById(id: Long): UserDto =
		userRepo.findById(id).map { getUserDto(it) }
			.orElseThrow { EntityNotFoundException("User with id $id was not found ") }
			.apply { image?.content = imageService.getContent(image?.name!!) }

	@Transactional
	override fun resetPassword(email: String) {
		val user = userRepo.findUserByEmail(email)
			.orElseThrow { EntityNotFoundException("User with email $email was not found ") }

		CompletableFuture.supplyAsync { tokenService.addPasswordTokenForUser(user) }
			.thenApply { emailService.sendResetPasswordEmail(email, it) }
	}

	@Transactional
	override fun update(userDto: UserDto): UserDto =
		userRepo.findById(userDto.id).map {
			saveOrUpdateUser(it.apply {
				login = userDto.login
			})
		}.orElseThrow { EntityNotFoundException("User with id ${userDto.id} not found ") }


	@Transactional
	override fun register(userCredentials: UserCredentials): UserDto {
		logger.info { "Trying to register user with email ${userCredentials.email}" }
		if (notExists(userCredentials)) {
			val image = imageService.getImage("default-user-pic")
			val mappedUser = mapper.map(userCredentials, User::class.java).apply {
				createdDate = LocalDateTime.now()
				password = encoder.encode(this.password)
				login = login ?: getLoginFromEmail()
				role = UserRoles.ROLE_USER
				this.image = image.let { Image(it.id, name = it.name!!) }
				isAccountNonLocked = true
				isEnabled = true
			}
			CompletableFuture.supplyAsync { emailService.sendWelcomingEmail(mappedUser.email) }
			logger.info { "Saving new user with email ${userCredentials.email}" }
			return saveOrUpdateUser(mappedUser).apply { this.image?.content = image.content }
		} else {
			logger.info { "User with email ${userCredentials.email} already exists" }
			throw EntityExistsException("Пользователь с имейлом ${userCredentials.email} уже существует")
		}
	}

	private fun notExists(userCredentials: UserCredentials) =
		userRepo.findUserByEmail(userCredentials.email).isEmpty

	@Transactional
	override fun addContent(ownerId: Long, content: Content): Content = when (content) {
		is WishDto -> addWish(ownerId, content)
		else -> throw UnknownContentTypeException("Ooops")
	}

	//	TODO refactoring
	private fun addWish(userId: Long, wishDto: WishDto): WishDto {
		val user = userRepo.getOne(userId)
		val wish = mapper.map(wishDto, Wish::class.java)
		wish.user = user
		wish.image = Image(wishDto.image?.id, name = wishDto.image?.name!!)
		user.wishes.add(wish)
		val savedUser = userRepo.save(user)
		return mapper.map(savedUser.wishes[savedUser.wishes.lastIndex], WishDto::class.java)
	}

	private fun saveOrUpdateUser(user: User) = getUserDto(userRepo.save(user))

	private fun getUserDto(user: User) = mapper.map(user, UserDto::class.java)

	private fun User.getLoginFromEmail() = "@" + email
		.replaceAfter("@", "")
		.removeSuffix("@")

}