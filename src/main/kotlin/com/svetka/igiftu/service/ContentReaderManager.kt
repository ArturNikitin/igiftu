package com.svetka.igiftu.service

import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.service.impl.ContentType

interface ContentReaderManager {
	fun getContent(userId: Long, username: String?, type: ContentType): PayloadDto
}