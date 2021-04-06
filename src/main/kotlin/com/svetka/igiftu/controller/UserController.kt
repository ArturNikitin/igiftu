package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user")
@CrossOrigin
class UserController(
    val userService: UserService
) {

    @GetMapping("{id}")
    fun getUser(@PathVariable id: Long) : UserDto {
        return userService.getUserById(id)
    }
}