package com.svetka.igiftu.service.common

interface StorageService {
	fun putFile(content: ByteArray, fileName: String) : String
	fun getFile(fileName: String) : ByteArray
}