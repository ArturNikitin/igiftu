package com.svetka.igiftu.service.manager.impl

import com.svetka.igiftu.dto.Content
import com.svetka.igiftu.exceptions.SecurityCreationException
import com.svetka.igiftu.exceptions.SecurityModificationException
import com.svetka.igiftu.service.Possessing
import com.svetka.igiftu.service.Updatable
import com.svetka.igiftu.service.entity.UserService
import com.svetka.igiftu.service.manager.ContentManager
import com.svetka.igiftu.service.manager.SecurityManager
import org.springframework.stereotype.Service

@Service
class ContentManagerImpl(
	private val securityManager: SecurityManager,
	private val userService: UserService
) : ContentManager {

	override fun create(userId: Long, content: Content, username: String, service: Updatable): Content {
		checkCreationPermission(userId, username, service)
		return create(userService, userId, content) { service.prepare(it) }
	}

	override fun update(
		userId: Long, contentId: Long, content: Content, username: String, service: Updatable
	): Content {
		checkModificationPermission(userId, contentId, username, service)
		return service.update(contentId, content)
	}

	override fun delete(userId: Long, contentId: Long, username: String, service: Updatable) {
		checkModificationPermission(userId, contentId, username, service)
		service.delete(contentId)
	}

	fun checkModificationPermission(userId: Long, contentId: Long, username: String, service: Updatable) {
		val modificationAllowed = securityManager.isModificationAllowed(userId, username, contentId, service)
		if (!modificationAllowed)
			throw SecurityModificationException("Illegal modification attempt")
	}


	fun checkCreationPermission(userId: Long, username: String, service: Updatable) {
		if (!securityManager.isOwner(userId, username))
			throw SecurityCreationException("Illegal creation attempt")
	}

	fun create(service: Possessing, ownerId: Long, content: Content, prepare: (Content) -> Content) =
		service.addContent(ownerId, prepare(content))
}