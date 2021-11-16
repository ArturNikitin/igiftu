package com.svetka.igiftu.component.wish

import com.svetka.igiftu.component.board.UserInfo
import com.svetka.igiftu.dto.WishDto

interface WishComponent {
	fun getWishById(id: Long): WishDto
	fun getWishesById(ids: Set<Long>): Iterable<WishDto>
	fun createWish(user: UserInfo, requestWish: WishDto): WishDto
}