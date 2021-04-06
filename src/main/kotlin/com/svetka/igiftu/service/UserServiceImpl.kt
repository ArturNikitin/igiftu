package com.svetka.igiftu.service

import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.entity.User
import com.svetka.igiftu.entity.enums.UserRoles
import com.svetka.igiftu.repository.UserRepository
import java.time.LocalDateTime
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder

@Service
class UserServiceImpl(
    val userRepo: UserRepository,
    val mapper: MapperFacade,
    val encoder: PasswordEncoder
) : UserService {
    override fun getUserById(id: Long): UserDto {
        val userById = userRepo.getUserById(id) ?: throw EntityNotFoundException()

        return getUserDto(userById)
    }
    
    override fun createUser(userDto: UserDto): UserDto {
        val mappedUser = mapper.map(userDto, User::class.java).apply {
            createdDate = LocalDateTime.now()
            password = userDto.password.let { encoder.encode(this.password) }
            login = email
            role = UserRoles.ROLE_USER
        }
        
        val savedUser = userRepo.save(mappedUser)
        
        return getUserDto(savedUser)
    }
    
    private fun getUserDto(user: User) = mapper.map(user, UserDto::class.java)
}