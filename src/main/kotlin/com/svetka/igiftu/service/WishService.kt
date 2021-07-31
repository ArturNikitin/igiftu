package com.svetka.igiftu.service

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.User
import java.security.Principal

interface WishService {
	fun getWishesByUserId(userId: Long): List<WishDto>
	fun getWishesCountByUserId(userId: Long): Long
	fun getWishById(wishId: Long) : WishDto
	fun prepareForCreation(wishDto: WishDto): WishDto
	fun delete(wishId: Long)
	fun update(contentId: Long, content: WishDto) : WishDto
}