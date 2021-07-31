package com.svetka.igiftu.service

import java.security.Principal

interface ContentManager {
	fun create(userId: Long, content: Any, principal: Principal) : Any
	fun update(userId: Long, contentId: Long, content: Any, principal: Principal) : Any
	fun delete(userId: Long, contentId: Long, content: String, principal: Principal)
}