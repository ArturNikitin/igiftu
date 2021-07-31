package com.svetka.igiftu.service

import com.svetka.igiftu.dto.Content
import com.svetka.igiftu.service.impl.ContentType

interface ContentManager {
	fun create(userId: Long, content: Content, username: String) : Content
	fun update(userId: Long, contentId: Long, content: Content, username: String) : Content
	fun delete(userId: Long, contentId: Long, content: ContentType, username: String)
}