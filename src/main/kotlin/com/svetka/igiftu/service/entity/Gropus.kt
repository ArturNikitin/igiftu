package com.svetka.igiftu.service.entity

import com.svetka.igiftu.dto.Content
import com.svetka.igiftu.service.manager.impl.OwnerType

interface Readable {
	fun get(ownerId: Long, ownerType: OwnerType): List<Content>
}

interface Updatable : Readable {
	fun prepare(content: Content): Content
	fun delete(contentId: Long)
	fun update(contentId: Long, content: Content): Content
	fun isOwner(ownerId: Long, contentId: Long?) : Boolean
}

interface Possessing {
	fun addContent(ownerId: Long, content: Content): Content
}

