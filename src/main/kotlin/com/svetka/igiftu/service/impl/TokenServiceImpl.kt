package com.svetka.igiftu.service.impl

import com.svetka.igiftu.entity.Token
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.repository.TokenRepository
import com.svetka.igiftu.service.TokenService
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class TokenServiceImpl(
    private val tokenRepo: TokenRepository
) : TokenService {

    override fun addPasswordTokenForUser(user: User): String {

        val token = tokenRepo.getByUserId(user.id!!)
            .also { updateToken(it) }
            .orElseGet { getToken(user) }

        return tokenRepo.save(token).passwordToken
    }

    private fun updateToken(token: Optional<Token>) {
        if (token.isPresent) {
            token.get().expirationDate = LocalDateTime.now().plusDays(1)
            token.get().passwordToken = UUID.randomUUID().toString().replace("-", "")
        }
    }

    private fun getToken(user: User) = Token(
        passwordToken = UUID.randomUUID().toString().replace("-", ""),
        expirationDate = LocalDateTime.now().plusDays(1),
        user = user
    )
}