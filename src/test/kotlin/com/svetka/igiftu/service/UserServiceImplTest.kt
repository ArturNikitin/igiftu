package com.svetka.igiftu.service

import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.verify
import ma.glasnost.orika.MapperFacade
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException

internal class UserServiceImplTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var mapper: MapperFacade

    @InjectMockKs
    private lateinit var userService: UserServiceImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }


    @Test
    fun getUserByIdSuccessTest() {
        every { userRepository.getUserById(1L) } returns getUser()
        every { userRepository.getUserById(2L) } returns getUser2()
        every { mapper.map(getUser(), UserDto::class.java) } returns getUserDto1()
        every { mapper.map(getUser2(), UserDto::class.java) } returns getUserDto2()

        val userById1 = userService.getUserById(1L)
        val userById2 = userService.getUserById(2L)

        assertEquals("email@gmail.com", userById1.email)
        assertEquals("login", userById1.login)
        assertEquals("email2@gmail.com", userById2.email)
        assertEquals("login2", userById2.login)


        verify {
            userRepository.getUserById(1L)
            userRepository.getUserById(2L)
            mapper.map(getUser(), UserDto::class.java)
            mapper.map(getUser2(), UserDto::class.java)
        }
    }

    @Test
    fun getUserByIdUserNotFoundTest() {
        every { userRepository.getUserById(1L) } returns null

        assertThrows(EntityNotFoundException::class.java) { userService.getUserById(1L) }
    }

    private fun getUser() = User(
        1L,
        LocalDateTime.now(),
        "1234",
        "email@gmail.com",
        "login",
        UserRoles.ROLE_USER,
        isEnabled = true,
        isAccountNonLocked = true
    )

    private fun getUser2() = User(
        2L,
        LocalDateTime.now(),
        "1234",
        "email2@gmail.com",
        "login2",
        UserRoles.ROLE_USER,
        isEnabled = true,
        isAccountNonLocked = true
    )

    private fun getUserDto1() = UserDto(
        "email@gmail.com",
        "login"
    )

    private fun getUserDto2() = UserDto(
        "email2@gmail.com",
        "login2"
    )
}