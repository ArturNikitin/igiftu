package com.svetka.igiftu.controller

import com.svetka.igiftu.service.WishService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/wish")
class WishController(
	private val wishService: WishService
) {
	
	@GetMapping("/{id}")
	fun getWish(@PathVariable id: Long) = wishService.getWishById(id)
}