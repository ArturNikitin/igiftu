package com.svetka.igiftu.repository

import com.svetka.igiftu.entity.Wish
import org.springframework.data.jpa.repository.JpaRepository

interface WishRepository : JpaRepository<Wish, Long> {
	fun getAllByUserId(userId: Long) : List<Wish>
	fun countByUserId(userId: Long): Long
}