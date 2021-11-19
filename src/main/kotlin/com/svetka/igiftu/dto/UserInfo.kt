package com.svetka.igiftu.dto

data class UserInfo(
	val id: Long,
	/**
	 *
	 * Email or login. If empty = unauth user
	 *
	 * */
	val username: String = ""
)
