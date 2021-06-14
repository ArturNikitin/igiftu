package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.service.WishService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/wish")
@CrossOrigin
class WishController(
        private val wishService: WishService
) {

    private val logger = KotlinLogging.logger { }

    @GetMapping("/{id}")
    fun getWish(@PathVariable id: Long) = wishService.getWishById(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createOrUpdate(@RequestBody wishDto: WishDto) = wishService.createWish(wishDto)

    @DeleteMapping("/{wishId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    fun deleteWish(@PathVariable wishId: Long, principal: Principal) {
        logger.info { "Received request to delete a wish with id {$wishId} and user ${principal.name}" }
        wishService.deleteWish(wishId)
        logger.info { "Completed request to delete wish with id with id {$wishId} and user ${principal.name}" }
    }
}