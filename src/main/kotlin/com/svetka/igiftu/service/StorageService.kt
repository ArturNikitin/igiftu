package com.svetka.igiftu.service

interface StorageService {
	fun putFile(content: ByteArray, fileName: String) : String
	fun getFile(fileName: String) : ByteArray
	fun deleteFile(fileName: String)
}