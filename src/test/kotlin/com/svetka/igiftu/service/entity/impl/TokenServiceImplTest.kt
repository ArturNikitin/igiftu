package com.svetka.igiftu.service.entity.impl

import com.svetka.igiftu.entity.Token
import com.svetka.igiftu.repository.TokenRepository
import com.svetka.igiftu.service.UserTest
import com.svetka.igiftu.service.entity.impl.TokenServiceImpl
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.mockk
import io.mockk.verify
import java.time.LocalDateTime
import java.util.Optional
import org.junit.jupiter.api.Test

internal class TokenServiceImplTest : UserTest() {

    private var tokenRepo: TokenRepository = mockk()

    @InjectMockKs
    private lateinit var tokenServiceImpl: TokenServiceImpl

    init {
        MockKAnnotations.init(this)
    }

    @Test
    fun addPasswordTokenForUser() {
        val user = getUser()
        val token = Token(
            passwordToken = "12345",
            expirationDate = LocalDateTime.now(),
            user = user
        )
        every { tokenRepo.getByUserId(1L) } returns Optional.of(token)
        every { tokenRepo.save(token) } returns token

        tokenServiceImpl.addPasswordTokenForUser(user)

        verify {
            tokenRepo.save(token)
            tokenRepo.getByUserId(1L)
        }
    }

    @Test
    fun addPasswordForUserFirstTime() {
        val user = getUser()
        val token = Token(
            passwordToken = "12345",
            expirationDate = LocalDateTime.now(),
            user = user
        )

        every { tokenRepo.getByUserId(1L) } returns Optional.empty()
        every { tokenRepo.save(token) } returns token

        tokenServiceImpl.addPasswordTokenForUser(user)

        verify {
            tokenRepo.save(token)
            tokenRepo.getByUserId(1L)
        }
    }
}