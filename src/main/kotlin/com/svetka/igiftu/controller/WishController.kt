package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.service.WishService
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

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
    fun deleteWish(@PathVariable wishId: Long) {
        wishService.deleteWish(wishId)
    }
}