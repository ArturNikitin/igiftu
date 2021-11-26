package com.svetka.igiftu.component.wish

import com.svetka.igiftu.dto.UpdateWishDto
import com.svetka.igiftu.dto.UserInfo
import com.svetka.igiftu.dto.WishDto

interface WishComponent {
	fun getWishById(id: Long): WishDto
	fun getWishesById(ids: Set<Long>): Iterable<WishDto>
	fun createWish(user: UserInfo, requestWish: WishDto): WishDto
	fun updateWish(user: UserInfo, requestWish: UpdateWishDto): WishDto
	fun deleteWish(user: UserInfo, wishId: Long)
	fun deleteWishes(user: UserInfo, wishes: Set<WishDto>)
}