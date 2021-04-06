package com.svetka.igiftu.service

import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.repository.UserRepository
import ma.glasnost.orika.MapperFacade
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException

@Service
class UserServiceImpl(
    val userRepo: UserRepository,
    val mapper: MapperFacade
) : UserService {
    override fun getUserById(id: Long): UserDto {
        val userById = userRepo.getUserById(id) ?: throw EntityNotFoundException()

        return mapper.map(userById, UserDto::class.java)
    }
}