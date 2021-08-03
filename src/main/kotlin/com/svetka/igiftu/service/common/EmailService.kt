package com.svetka.igiftu.service.common

interface EmailService {
	fun sendEmail(email: String)
    fun sendResetPasswordEmail(email: String, token: String)
}