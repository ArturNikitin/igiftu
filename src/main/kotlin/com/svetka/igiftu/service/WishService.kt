package com.svetka.igiftu.service

import com.svetka.igiftu.dto.WishDto

interface WishService {
	fun getWishesByUserId(id: Long): List<WishDto>
	fun getWishesCountByUser(id: Long): Long
}