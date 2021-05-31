package com.svetka.igiftu.repository

import com.svetka.igiftu.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun getUserByEmail(email: String): Optional<User>
    fun existsByIdOrEmail(id: Long, email: String): Boolean
}