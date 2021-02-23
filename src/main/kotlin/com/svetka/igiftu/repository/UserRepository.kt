package com.svetka.igiftu.repository

import com.svetka.igiftu.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun getUserByEmail(email: String): User?
    fun getUserById(id: Long): User?
}