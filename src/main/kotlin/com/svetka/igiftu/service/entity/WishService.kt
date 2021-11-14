package com.svetka.igiftu.service.entity

import com.svetka.igiftu.dto.WishDto

interface WishService : Updatable {
	fun getWishes(ids: Iterable<Long>): Iterable<WishDto>
}