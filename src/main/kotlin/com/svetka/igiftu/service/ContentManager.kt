package com.svetka.igiftu.service

import com.svetka.igiftu.dto.Content

interface ContentManager {
	fun create(userId: Long, content: Content, username: String, service: Updatable): Content
	fun update(userId: Long, contentId: Long, content: Content, username: String, service: Updatable): Content
	fun delete(userId: Long, contentId: Long, username: String, service: Updatable)
}