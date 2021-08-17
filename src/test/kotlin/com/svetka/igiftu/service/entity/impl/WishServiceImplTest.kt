package com.svetka.igiftu.service.entity.impl

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.entity.enums.Access
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.repository.WishRepository
import com.svetka.igiftu.service.UserTest
import com.svetka.igiftu.service.entity.impl.WishServiceImpl
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import java.time.LocalDateTime
import ma.glasnost.orika.MapperFacade
import org.junit.jupiter.api.BeforeEach

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


    @InjectMockKs
    private lateinit var wishService: WishServiceImpl

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
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