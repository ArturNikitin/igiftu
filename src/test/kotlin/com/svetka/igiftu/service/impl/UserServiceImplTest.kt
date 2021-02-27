package com.svetka.igiftu.service.impl

import com.svetka.igiftu.repository.UserRepository
import com.svetka.igiftu.service.UserDto
import com.svetka.igiftu.service.UserService
import io.mockk.mockk
import ma.glasnost.orika.MapperFacade
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder


internal class UserServiceImplTest {


    private var userRepo: UserRepository = mockk()

    private var mapper: MapperFacade = mockk()

    private var encoder: PasswordEncoder = mockk()

    var service: UserService = UserServiceImpl(userRepo, mapper, encoder)

    @BeforeEach
    fun setUp() {

    }

    private fun getUserDto() = UserDto(email = "artur00794@gmail.com", password = "1234")

    @Test
    fun createUserSuccess() {

    }
}