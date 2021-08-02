package com.svetka.igiftu.service

import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.service.interfaces.Updatable

interface WishService : Updatable {
	fun getWishesByUserId(userId: Long): List<WishDto>
	fun getWishesCountByUserId(userId: Long): Long
	fun getWishById(wishId: Long) : WishDto
}