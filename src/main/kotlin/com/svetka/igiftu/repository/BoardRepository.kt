package com.svetka.igiftu.repository

import com.svetka.igiftu.entity.Board
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository : JpaRepository<Board, Long> {
}