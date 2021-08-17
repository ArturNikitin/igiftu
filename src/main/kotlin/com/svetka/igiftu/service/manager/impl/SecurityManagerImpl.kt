package com.svetka.igiftu.service.manager.impl

import com.svetka.igiftu.service.manager.SecurityManager
import com.svetka.igiftu.service.Updatable
import com.svetka.igiftu.service.entity.UserService
import org.springframework.stereotype.Service

@Service
class SecurityManagerImpl(
	private val userService: UserService
) : SecurityManager {
	override fun isModificationAllowed(userId: Long, username: String, contentId: Long, service: Updatable) =
		getPermission(userId, username, contentId) { l: Long, l1: Long? -> service.isOwner(l, l1) }
			.all { it }

	override fun isCreationAllowed(userId: Long, username: String, service: Updatable) =
		getPermission(userId, username, null) { l: Long, l1: Long? -> service.isOwner(l, l1) }
			.any { it }

	override fun isOwner(userId: Long, username: String?): Boolean = isSameUser(userId, username)

	fun test(userId: Long, username: String?, contentId: Long?, service: Updatable) {
		getPermission(userId, username, contentId) { l: Long, l1: Long? -> service.isOwner(l, l1) }
	}

	private fun getPermission(
		userId: Long,
		username: String?,
		contentId: Long?,
		action: (ownerId: Long, contentId: Long?) -> Boolean
	): List<Boolean> {
		val result = mutableListOf<Boolean>()
		result.add(action(userId, contentId))
		result.add(isSameUser(userId, username))

		return result
	}

	private fun isSameUser(userId: Long, username: String?): Boolean {
		username ?: return false
		val user = userService.getUserById(userId)

		return user.email == username || user.login == username
	}
}