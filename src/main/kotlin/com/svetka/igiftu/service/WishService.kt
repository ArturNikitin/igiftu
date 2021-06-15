package com.svetka.igiftu.service

import com.svetka.igiftu.dto.WishDto
import java.security.Principal

interface WishService {
	fun getWishesByUserId(userId: Long): List<WishDto>
	fun getWishesCountByUserId(userId: Long): Long
	fun getWishById(wishId: Long) : WishDto
	fun createWish(wishDto: WishDto): WishDto
	fun deleteWish(wishId: Long, principal: Principal)
}