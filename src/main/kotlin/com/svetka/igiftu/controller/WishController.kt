package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.service.ContentManager
import com.svetka.igiftu.service.WishService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("user/{userId}/wishNew")
@CrossOrigin
class WishController(
        private val wishService: WishService,
        private val contentManager: ContentManager
) {

    private val log = KotlinLogging.logger { }

    @GetMapping("/{wishId}")
    fun getWish(@PathVariable wishId: Long) = wishService.getWishById(wishId)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    fun createWish(
    @PathVariable userId: Long,
    @RequestBody wishDto: WishDto,
    principal: Principal)
    : Any {
        log.info { "Received request to create wish for user {$userId} and data {$wishDto}" }
        //todo update wish
        val wish = contentManager.create(userId, wishDto, principal)
        log.info { "Finished request to create wish for user {$userId} and data {$wish}" }
        return wish
    }

    @PutMapping("/{wishId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun updateWish(
        @PathVariable userId: Long,
        @PathVariable wishId: Long,
        @RequestBody wishDto: WishDto,
        principal: Principal)
    : Any {
        log.info { "Received request to update wish {$wishDto} for user {$userId} and data {$wishDto}" }
        //todo update wish
        val wish = contentManager.update(userId, wishId, wishDto, principal)
        log.info { "Finished request to update wish {$wishId} for user {$userId} and data {$wish}" }
        return wish
    }

    @DeleteMapping("/{wishId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    fun deleteWish(@PathVariable wishId: Long, principal: Principal) {
        log.info { "Received request to delete a wish with id {$wishId} and user ${principal.name}" }
        wishService.delete(wishId, principal)
        log.info { "Completed request to delete wish with id with id {$wishId} and user ${principal.name}" }
    }
}