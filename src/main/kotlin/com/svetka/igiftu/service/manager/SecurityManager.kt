package com.svetka.igiftu.service.manager

import com.svetka.igiftu.service.Updatable
import org.springframework.stereotype.Service

@Service
interface SecurityManager {
	fun isModificationAllowed(userId: Long, username: String, contentId: Long, service: Updatable): Boolean
	fun isCreationAllowed(userId: Long, username: String, service: Updatable): Boolean
	fun isOwner(userId: Long, username: String?): Boolean
}