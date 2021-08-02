package com.svetka.igiftu.service

import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.service.impl.ContentType
import com.svetka.igiftu.service.interfaces.Updatable

interface ReaderManager {
	fun getUserContent(userId: Long, username: String?, type: ContentType): PayloadDto
}