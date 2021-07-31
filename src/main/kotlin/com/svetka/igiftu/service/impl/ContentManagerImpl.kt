package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.service.ContentManager
import com.svetka.igiftu.service.SecurityManager
import com.svetka.igiftu.service.UserService
import com.svetka.igiftu.service.WishService
import org.springframework.stereotype.Service
import java.security.Principal
import javax.persistence.EntityNotFoundException

@Service
class ContentManagerImpl(
	private val securityManager: SecurityManager,
	private val wishService: WishService,
	private val userService: UserService
) : ContentManager {
	//	TODO
	override fun create(userId: Long, content: Any, principal: Principal): Any {
		checkCreationPermission(userId, principal)
		return when (content) {
			is WishDto -> userService.addWish(userId, wishService.create(content))
			else -> throw EntityNotFoundException("") //TODO create exception
		}
	}

	//	TODO
	override fun update(userId: Long, contentId: Long, content: Any, principal: Principal): Any {
		checkModificationPermission(userId, contentId, principal)
		return when (content) {
			is WishDto -> wishService.update(contentId, content)
			else -> throw EntityNotFoundException("") //TODO create exception
		}
	}

	//	TODO ad enum
	override fun delete(userId: Long, contentId: Long, content: String, principal: Principal) {
		checkModificationPermission(userId, contentId, principal)
		when (content) {
			"wish" -> wishService.delete(contentId)
		}
	}

	fun checkModificationPermission(userId: Long, contentId: Long, principal: Principal) =
		securityManager.isModificationAllowed(userId, contentId, principal)
			.also { if (!it) throw EntityNotFoundException("") } //todo good exception

	fun checkCreationPermission(userId: Long, principal: Principal) =
		securityManager.isCreationAllowed(userId, principal)
			.also { if (!it) throw EntityNotFoundException("") } //todo good exception
}