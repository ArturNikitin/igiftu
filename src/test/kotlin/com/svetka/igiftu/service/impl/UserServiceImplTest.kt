package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.repository.UserRepository
import com.svetka.igiftu.service.EmailService
import com.svetka.igiftu.service.TokenService
import com.svetka.igiftu.service.UserTest
import com.svetka.igiftu.service.WishService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import java.util.Optional
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException
import ma.glasnost.orika.MapperFacade
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder
import com.svetka.igiftu.service.impl.WishServiceImplTest.Companion as WishServiceImplTest1

internal class UserServiceImplTest : UserTest() {

    @MockK
    private lateinit var tokenService: TokenService

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var mapper: MapperFacade

    @MockK
    private lateinit var encoder: PasswordEncoder

    @MockK
    private lateinit var emailService: EmailService

    @MockK
    private lateinit var wishService: WishService

    @InjectMockKs
    private lateinit var userService: UserServiceImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        every { encoder.encode(password1) } returns "XXXXXXXXXXXXXX"

        every { userRepository.existsByIdOrEmail(id = 1L, "") } returns true

        every { userRepository.existsByIdOrEmail(id = 2L, "") } returns false

        every { userRepository.existsByIdOrEmail(0L, email1) } returns true

        every { userRepository.existsByIdOrEmail(0L, email2) } returns false
    }

    @Test
    fun resetPasswordSuccess() {
        every { userRepository.getUserByEmail(email1) } returns Optional.of(getUser())

        userService.resetPassword(email1)

        verify {
            userRepository.getUserByEmail(email1)
            tokenService.addPasswordTokenForUser(getUser())
        }
    }

    @Test
    fun resetPasswordUserDoesNotExist() {
        every { userRepository.getUserByEmail(email1) } returns Optional.empty()

        assertThrows(EntityNotFoundException::class.java) { userService.resetPassword(email1) }
    }

    @Test
    fun getAllWishesByUserIdSuccessIsOwnerWithWishes() {
        every { wishService.getWishesByUserId(1L) } returns
            listOf(WishServiceImplTest1.getWishDto(), WishServiceImplTest1.getSecondWishDto())

        val payloadDto = userService.getAllWishesByUserId(1L)

        assertNotNull(payloadDto)
        assertEquals(true, payloadDto.isOwner)
        assertEquals(2, payloadDto.payload.size)

        verify {
            userRepository.existsByIdOrEmail(1L, "")
        }
    }

    @Test
    fun getAllWishesByUserIdSuccessIsOwnerNoWishes() {
        every { wishService.getWishesByUserId(1L) } returns emptyList()

        val payloadDto = userService.getAllWishesByUserId(1L)

        assertNotNull(payloadDto)
        assertEquals(true, payloadDto.isOwner)
        assertEquals(emptyList<WishDto>(), payloadDto.payload)

        verify {
            userRepository.existsByIdOrEmail(id = 1L, email = "")
            wishService.getWishesByUserId(1L)
        }
    }

    @Test
    fun getAllWishesByUserIdUserNotFound() {
        assertThrows(EntityNotFoundException::class.java) { userService.getAllWishesByUserId(2L) }
    }

    @Test
    fun getUserByIdSuccessTest() {
        every { userRepository.findById(1L) } returns Optional.of(getUser())
        every { userRepository.findById(2L) } returns Optional.of(getUser2())
        every { mapper.map(getUser(), UserDto::class.java) } returns getUserDto1()
        every { mapper.map(getUser2(), UserDto::class.java) } returns getUserDto2()

        val userById1 = userService.getUserById(1L)
        val userById2 = userService.getUserById(2L)

        assertEquals(email1, userById1.email)
        assertEquals(login1, userById1.login)
        assertEquals(email2, userById2.email)
        assertEquals(login2, userById2.login)


        verify {
            userRepository.findById(1L)
            userRepository.findById(2L)
            mapper.map(getUser(), UserDto::class.java)
            mapper.map(getUser2(), UserDto::class.java)
        }
    }

    @Test
    fun getUserByIdUserNotFoundTest() {
        every { userRepository.findById(1L) } returns Optional.empty()

        assertThrows(EntityNotFoundException::class.java) { userService.getUserById(1L) }
    }

    @Test
    fun updateUserExistsTest() {
        every { userRepository.findById(1L) } returns Optional.of(getUser())
        every {
            userRepository.save(getUser().apply {
                login = login2
            })
        } returns getUserAfterUpdate()
        every { mapper.map(getUserAfterUpdate(), UserDto::class.java) } returns getUserDtoToUpdate()

        val updateUser = userService.updateUser(getUserDtoToUpdate())

        assertNotNull(updateUser)
        assertEquals(email1, updateUser.email)
        assertEquals(login2, updateUser.login)

        verify {
            userRepository.findById(1L)
            mapper.map(getUserAfterUpdate(), UserDto::class.java)
            userRepository.save(getUserAfterUpdate())
        }

    }

    @Test
    fun updateUserDoesNotExist() {
        every { userRepository.findById(1L) } returns Optional.empty()

        assertThrows(EntityNotFoundException::class.java) { userService.updateUser(getUserDtoToUpdate()) }
    }

    @Test
    fun registerUser() {
        every { mapper.map(getUserCreds(), User::class.java) } returns (getUserToSave())
        every { userRepository.save(getUserToSave()) } returns getUser()
        every { mapper.map(getUser(), UserDto::class.java) } returns getUserDto1()
        every { userRepository.getUserByEmail(getUserCreds().email) } returns Optional.empty()
        every { emailService.sendEmail(getUserCreds().email) }

        val registeredUser = userService.registerUser(getUserCreds())

        assertNotNull(registeredUser)
        assertEquals(email1, registeredUser.email)
        assertEquals(login1, registeredUser.login)

        verify {
            mapper.map(getUserCreds(), User::class.java)
            mapper.map(getUser(), UserDto::class.java)
            userRepository.save(getUserToSave())
            encoder.encode(password1)
            userRepository.getUserByEmail(email1)
            emailService.sendEmail(getUserCreds().email)
        }
    }

    @Test
    fun registerDuplicatedUser() {
        every { userRepository.getUserByEmail(getUserCreds().email) } returns Optional.of(getUser())

        assertThrows(
            EntityExistsException::class.java
        ) { userService.registerUser(getUserCreds()) }
    }

}