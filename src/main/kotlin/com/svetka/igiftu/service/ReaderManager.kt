package com.svetka.igiftu.service

import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.dto.RequestDto

interface ReaderManager {
	fun getContent(requestDto: RequestDto): PayloadDto
}