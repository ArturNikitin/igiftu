package com.svetka.igiftu.service

interface EmailService {
	fun sendWelcomingEmail(email: String)
    fun sendResetPasswordEmail(email: String, token: String)
}