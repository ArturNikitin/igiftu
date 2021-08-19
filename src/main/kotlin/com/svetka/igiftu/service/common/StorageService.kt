package com.svetka.igiftu.service.common

import java.util.UUID

interface StorageService {
	fun putFile(content: ByteArray, fileName: String) : String
	fun getFile(fileName: String) : ByteArray
}