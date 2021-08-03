package com.svetka.igiftu.service.common

import com.svetka.igiftu.entity.User


interface TokenService {
    fun addPasswordTokenForUser(user: User) : String
    fun verifyToken(token: String) : User
}