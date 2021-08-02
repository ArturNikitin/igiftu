package com.svetka.igiftu.service.interfaces

import com.svetka.igiftu.dto.Content

interface Updatable {
	fun prepare(content: Content): Content
	fun delete(contentId: Long)
	fun update(contentId: Long, content: Content): Content
	fun isOwner(ownerId: Long, contentId: Long?) : Boolean
}

interface Possessing {
	fun addContent(ownerId: Long, content: Content): Content
}

