package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.service.WishService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/wish")
class WishController(
	private val wishService: WishService
) {
	
	@GetMapping("/{id}")
	fun getWish(@PathVariable id: Long) = wishService.getWishById(id)
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun createOrUpdate(@RequestBody wishDto: WishDto) = wishService.createWish(wishDto)
}