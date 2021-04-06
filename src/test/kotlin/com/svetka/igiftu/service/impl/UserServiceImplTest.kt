package com.svetka.igiftu.service.impl

import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.repository.UserRepository
import com.svetka.igiftu.service.UserServiceImpl
import com.svetka.igiftu.service.UserTest
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import javax.persistence.EntityNotFoundException
import ma.glasnost.orika.MapperFacade
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

internal class UserServiceImplTest : UserTest() {
	
	@MockK
	private lateinit var userRepository: UserRepository
	
	@MockK
	private lateinit var mapper: MapperFacade
	
	@MockK
	private lateinit var encoder: PasswordEncoder
	
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
	
	@Test
	fun createUserSuccessTest() {
		every { mapper.map(getUserDtoToSave(), User::class.java) } returns getUserToSaveFirst()
		every { userRepository.save(getUserToSave()) } returns getUser()
        every { mapper.map(getUser(), UserDto::class.java) } returns getUserDto2()
		every { encoder.encode("1234") } returns "XXXXXXXXXXXX"
		
		val createUser = userService.createUser(getUserDtoToSave())
		
		assertNotNull(createUser)
		println(createUser)
		
		verify { userRepository.save(getUserToSave()) }
		verify { encoder.encode("1234") }
	}
}