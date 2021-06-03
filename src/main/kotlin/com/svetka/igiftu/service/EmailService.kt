package com.svetka.igiftu.service

interface EmailService {
	fun sendEmail(email: String)
    fun sendResetPasswordEmail(email: String, token: String)
}