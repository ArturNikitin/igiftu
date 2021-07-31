package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.entity.enums.Access
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.exceptions.ItemDoesNotBelongToUser
import com.svetka.igiftu.repository.WishRepository
import com.svetka.igiftu.service.UserService
import com.svetka.igiftu.service.UserTest
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import java.time.LocalDateTime
import java.util.Optional
import javax.persistence.EntityNotFoundException
import ma.glasnost.orika.MapperFacade
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.security.Principal

internal class WishServiceImplTest {

    companion object {
        fun createWishDto() = WishDto(
                name = "Create wish"
        )

        fun getWishDto() = WishDto(
                name = "My wish",
                price = 17.00,
                access = Access.PUBLIC.name
        )

        fun getSecondWishDto() = WishDto(
                name = "My second Wish",
                price = 15.00,
                access = Access.PUBLIC.name
        )
    }

    @MockK
    private lateinit var wishRepository: WishRepository

    @MockK
    private lateinit var mapper: MapperFacade

    @MockK
    private lateinit var userService: UserService

    @InjectMockKs
    private lateinit var wishService: WishServiceImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getWishById() {
        every { wishRepository.findById(1L) } returns Optional.of(getWish())
        every { mapper.map(getWish(), WishDto::class.java) } returns getWishDto()

        val wishDto = wishService.getWishById(1L)

        assertEquals(getWish().name, wishDto.name)

        verify {
            wishRepository.findById(1L)
            mapper.map(getWish(), WishDto::class.java)
        }
    }

    @Test
    fun getWishByIdEntityNotFoundException() {
        every { wishRepository.findById(1L) } returns Optional.empty()

        assertThrows(EntityNotFoundException::class.java) { wishService.getWishById(1L) }
    }

    @Test
    fun getWishesByUserIdTest() {
        every { wishRepository.getAllByUserId(1L) } returns listOf(getWish(), getSecondWish())
        every { mapper.map(getWish(), WishDto::class.java) } returns getWishDto()
        every { mapper.map(getSecondWish(), WishDto::class.java) } returns getSecondWishDto()

        val wishesDtoByUserId = wishService.getWishesByUserId(1L)

        assertEquals(2, wishesDtoByUserId.size)
        assertEquals(getWishDto(), wishesDtoByUserId[0])
        assertEquals(getSecondWishDto(), wishesDtoByUserId[1])


        verify {
            wishRepository.getAllByUserId(1L)
            mapper.map(getWish(), WishDto::class.java)
            mapper.map(getSecondWish(), WishDto::class.java)
        }

    }

    @Test
    fun getWishesByUserNoWishesTest() {
        every { wishRepository.getAllByUserId(1L) } returns emptyList()

        val wishesByUserId = wishService.getWishesByUserId(1L)

        assertEquals(0, wishesByUserId.size)
    }

    @Test
    fun getWishesCountByUserTest() {
        every { wishRepository.countByUserId(1L) } returns 10L

        val wishesCountByUser = wishService.getWishesCountByUserId(1L)

        assertEquals(10L, wishesCountByUser)
    }


    private fun getSecondWish() = Wish(
            name = "My second Wish",
            createdDate = LocalDateTime.now(),
            lastModifiedDate = LocalDateTime.now(),
            user = User(
                    1L,
                    LocalDateTime.now(),
                    UserTest.password1,
                    UserTest.email1,
                    UserTest.login1,
                    UserRoles.ROLE_USER,
                    isEnabled = true,
                    isAccountNonLocked = true
            )
    )

    private fun getWish() = Wish(
            name = "My wish",
            createdDate = LocalDateTime.now(),
            lastModifiedDate = LocalDateTime.now(),
            user = User(
                    1L,
                    LocalDateTime.now(),
                    UserTest.password1,
                    UserTest.email1,
                    UserTest.login1,
                    UserRoles.ROLE_USER,
                    isEnabled = true,
                    isAccountNonLocked = true
            )
    )

}