package com.svetka.igiftu.component.user

import com.svetka.igiftu.dto.BoardDto
import com.svetka.igiftu.dto.PasswordDto
import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.dto.UserInfo
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Board
import com.svetka.igiftu.entity.Image
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.exceptions.SecurityModificationException
import com.svetka.igiftu.repository.UserRepository
import com.svetka.igiftu.service.common.EmailService
import com.svetka.igiftu.service.entity.ImageService
import com.svetka.igiftu.service.entity.TokenService
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
class UserService(
	private val userRepo: UserRepository,
	private val mapper: MapperFacade,
	private val encoder: PasswordEncoder,
	private val emailService: EmailService,
	private val tokenService: TokenService,
	private val imageService: ImageService
) : UserComponent {
	private val logger = KotlinLogging.logger { }

	override fun isSameUser(userId: Long, username: String): Boolean {
		val user = getUserIfExists(userId)
		return user.email == username || user.login == username
	}

	@Transactional
	override fun getUserById(id: Long): UserDto =
		userRepo.findById(id).map { getUserDto(it) }
			.orElseThrow { EntityNotFoundException("User with id $id was not found ") }
			.apply { image?.content = imageService.getContent(image?.name!!) }

	@Transactional
	override fun update(userDto: UserDto, username: String): UserDto {
		val user = userRepo.findById(userDto.id)
			.orElseThrow { EntityNotFoundException("User with id ${userDto.id} not found ") }
			.also {
				if (it.email != username && it.login != username)
					throw SecurityModificationException("Illegal modification attempt")
			}
		val imageDto = userDto.image?.content?.let {
			imageService.saveImage(
				it
			)
		}
		return saveOrUpdateUser(
			user.apply {
				image = Image(imageDto?.id, name = imageDto?.name!!)
				login = userDto.login
			}
		)
	}

	@Transactional
	override fun register(userCredentials: UserCredentials): UserDto {
		logger.info { "Trying to register user with email ${userCredentials.email}" }
		if (notExists(userCredentials)) {
			val image = imageService.getDefaultImage("default-user-pic")
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

	@Transactional
	override fun requestResetPassword(email: String) {
		val user = userRepo.findUserByEmail(email)
			.orElseThrow { EntityNotFoundException("User with email $email was not found ") }

		CompletableFuture.supplyAsync { tokenService.addPasswordTokenForUser(user) }
			.thenApply { emailService.sendResetPasswordEmail(email, it) }
	}

	@Transactional
	override fun updatePassword(password: PasswordDto) {
		val user = tokenService.verifyToken(password.token)
		user.password = encoder.encode(password.password)
		userRepo.save(user)
	}

	@Transactional
	override fun addWishes(userId: Long, wishes: Set<Wish>): Set<WishDto> {
		return getUserIfExists(userId)
			.apply {
				wishes.forEach { it.user = this }
				this.wishes.addAll(wishes)
			}
			.let { userRepo.save(it) }
			.wishes
			.map { mapper.map(it, WishDto::class.java) }
			.toSet()
	}

	override fun deleteWishes(userId: Long, wishes: Set<Wish>): Set<WishDto> {
		TODO("Not yet implemented")
	}

	@Transactional
	override fun addBoards(userId: Long, boards: Set<Board>): Set<BoardDto> {
		return getUserIfExists(userId)
			.apply {
				boards.forEach { it.user = this }
				this.boards.addAll(boards)
			}
			.let { userRepo.save(it) }
			.boards
			.map { mapper.map(it, BoardDto::class.java) }
			.toSet()
	}

	override fun deleteBoards(userId: Long, boards: Set<WishDto>) {
		TODO("Not yet implemented")
	}

	@Transactional
	override fun getWishes(userId: Long): Set<WishDto> {
		return getUserIfExists(userId)
			.wishes
			.map { mapper.map(it, WishDto::class.java) }
			.toSet()
	}

	private fun saveOrUpdateUser(user: User) = getUserDto(userRepo.save(user))

	private fun getUserDto(user: User) = mapper.map(user, UserDto::class.java)

	private fun User.getLoginFromEmail() = "@" + email
		.replaceAfter("@", "")
		.removeSuffix("@")

	private fun getUserIfExists(id: Long): User =
		userRepo.findById(id)
			.orElseThrow { EntityNotFoundException("User with id $id was not found ") }

	private fun notExists(userCredentials: UserCredentials) =
		userRepo.findUserByEmail(userCredentials.email).isEmpty
}