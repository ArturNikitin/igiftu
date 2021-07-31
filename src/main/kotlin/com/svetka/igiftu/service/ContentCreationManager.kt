package com.svetka.igiftu.service

import com.svetka.igiftu.dto.Content
import java.security.Principal

interface ContentCreationManager {
	fun create(userId: Long, content: Content, principal: Principal) : Content
	fun update(userId: Long, contentId: Long, content: Content, principal: Principal) : Content
	fun delete(userId: Long, contentId: Long, content: String, principal: Principal)
}