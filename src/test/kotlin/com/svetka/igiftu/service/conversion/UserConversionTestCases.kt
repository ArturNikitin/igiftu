package com.svetka.igiftu.service.conversion

import com.svetka.igiftu.config.MapperConfig
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.UserRoles
import ma.glasnost.orika.MapperFactory
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime


class UserConversionTestCases {

    @Test
    fun userToUserDto() {
        val factory: MapperFactory = MappingUtils.getMapperFactory(false)
        val mapper = factory.mapperFacade

        val mappedUser = mapper.map(getUser(), UserDto::class.java)

        assertEquals("email@gmail.com", mappedUser.email)
        assertEquals("login", mappedUser.login)

        println(mappedUser)
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

    private fun userDto() = UserDto(
        1L,
        email =  "email@gmail.com",
        login =  "login",
        role = UserRoles.ROLE_USER.name,
    )
}