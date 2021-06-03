package com.svetka.igiftu.service

import com.svetka.igiftu.entity.User


interface TokenService {
    fun addPasswordTokenForUser(user: User) : String
}