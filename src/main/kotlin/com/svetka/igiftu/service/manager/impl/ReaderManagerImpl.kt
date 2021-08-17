package com.svetka.igiftu.service.manager.impl

import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.dto.RequestDto
import com.svetka.igiftu.service.manager.ReaderManager
import com.svetka.igiftu.service.manager.SecurityManager
import org.springframework.stereotype.Service

@Service
class ReaderManagerImpl(
	private val securityManager: SecurityManager
) : ReaderManager {

	override fun getContent(requestDto: RequestDto): PayloadDto {
		return PayloadDto(
			securityManager.isOwner(requestDto.userId, requestDto.username),
			requestDto.service.get(requestDto.ownerId, requestDto.ownerType)
		)
	}
}