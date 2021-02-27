package com.svetka.igiftu.service.impl

import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.repository.UserRepository
import com.svetka.igiftu.service.UserDto
import com.svetka.igiftu.service.UserService
import io.mockk.every
import io.mockk.mockk
import ma.glasnost.orika.MapperFacade
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


internal class UserServiceImplTest {


	private var userRepo: UserRepository = mockk()

	private var mapper: MapperFacade = mockk()

	private var encoder: PasswordEncoder = mockk()

	var service: UserService = UserServiceImpl(userRepo, mapper, encoder)

	@BeforeEach
	fun setUp() {
	}

	private fun getUserDto() = UserDto(email = "artur@gmail.com", password = "1234")

	private fun getUserUnsaved() = User(
		email = "artur@gmail.com",
		password = "1234",
		isAccountNonLocked = true,
		isEnabled = true
	)

	private fun getSavedUser() = User(
		1L,
		null,
		"super secret",
		"artur@gmail.com",
		"@artur",
		UserRoles.ROLE_USER,
		isEnabled = true,
		isAccountNonLocked = true
	)

	private fun getSavedUserDto() = UserDto(
		email = "artur@gmail.com",
		password = "super secret",
		"@artur",
		1L
	)


	@Test
	fun createUserSuccess() {
//        given
		every { mapper.map(getUserDto(), User::class.java) } returns getUserUnsaved()
		every { encoder.encode("1234") } returns "super secret"
		every { userRepo.save(any()) } returns getSavedUser()
		every { mapper.map(getSavedUser(), UserDto::class.java) } returns getSavedUserDto()
//        when
		val createUser = service.createUser(getUserDto())
//        then
		assertNotNull(createUser)
		assertEquals(getSavedUser().password, "super secret")
	}
}