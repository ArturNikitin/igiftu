package com.svetka.igiftu.service.entity

import com.svetka.igiftu.entity.User


interface TokenService {
    fun addPasswordTokenForUser(user: User) : String
    fun verifyToken(token: String) : User
}