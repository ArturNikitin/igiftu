package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.EmailDto
import com.svetka.igiftu.dto.PasswordDto
import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.dto.UserCredentials
import com.svetka.igiftu.dto.UserDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.service.UserService
import javax.validation.Valid
import mu.KotlinLogging
import org.springframework.http.HttpStatus.CREATED
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
@CrossOrigin
class UserController(
    private val userService: UserService
) {
    private val logger = KotlinLogging.logger { }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getUser(@PathVariable id: Long): UserDto {
        return userService.getUserById(id)
    }

    @PostMapping
    fun createUser(@RequestBody user: UserDto) = userService.updateUser(user)

    @PostMapping("/registration")
    @ResponseStatus(CREATED)
    fun registerUser(@Valid @RequestBody user: UserCredentials) = userService.registerUser(user)

    @GetMapping("/{userId}/wish")
    fun getAllWishesByUserId(@PathVariable userId: Long): PayloadDto {
        logger.info { "Request to get all wishes for user with id $userId" }
        return userService.getAllWishesByUserId(userId)
    }

    @PostMapping("/{userId}/wish")
    @ResponseStatus(CREATED)
    fun createWishConnectedToUser(
        @PathVariable userId: Long,
        @RequestBody wishDto: WishDto
    ): WishDto {
        return userService.createWish(userId, wishDto)
    }

    @PostMapping("/password")
    fun resetPassword(@RequestBody email: EmailDto): String {
        userService.resetPassword(email.email)

       return "На ваш имейл отправлены ссылка для перехода обновление пароля"
    }

    @PostMapping("/password/update")
    fun updatePassword(
        @RequestBody passwordDto: PasswordDto) : String {
        userService.updatePassword(passwordDto)

        return "Ваш пароль был успешно изменен"
    }
}