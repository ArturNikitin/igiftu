package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.Content
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.service.ContentCreationManager
import com.svetka.igiftu.service.SecurityManager
import com.svetka.igiftu.service.UserService
import com.svetka.igiftu.service.WishService
import javax.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class ContentCreationManagerImpl(
	private val securityManager: SecurityManager,
	private val wishService: WishService,
	private val userService: UserService
) : ContentCreationManager {
	//	TODO
	override fun create(userId: Long, content: Content, username: String): Content {
		checkCreationPermission(userId, username)
		return when (content) {
			is WishDto -> userService.addWish(userId, wishService.create(content))
			else -> throw EntityNotFoundException("") //TODO create exception
		}
	}

	//	TODO
	override fun update(userId: Long, contentId: Long, content: Content, username: String): Content {
		checkModificationPermission(userId, contentId, username)
		return when (content) {
			is WishDto -> wishService.update(contentId, content)
			else -> throw EntityNotFoundException("") //TODO create exception
		}
	}

	//	TODO ad enum
	override fun delete(userId: Long, contentId: Long, content: ContentType, username: String) {
		checkModificationPermission(userId, contentId, username)
		when (content) {
			ContentType.WISH -> wishService.delete(contentId)
			ContentType.BOARD -> TODO()
		}
	}

	fun checkModificationPermission(userId: Long, contentId: Long, username: String) =
		securityManager.isModificationAllowed(userId, contentId, username)
			.also { if (!it) throw EntityNotFoundException("") } //todo good exception

	fun checkCreationPermission(userId: Long, username: String) =
		securityManager.isCreationAllowed(userId, username)
			.also { if (!it) throw EntityNotFoundException("") } //todo good exception
}