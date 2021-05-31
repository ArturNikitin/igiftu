package com.svetka.igiftu.repository

import com.svetka.igiftu.entity.Token
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository


interface TokenRepository : JpaRepository<Token, Long> {
    fun getByUserId(userId: Long) : Optional<Token>
}