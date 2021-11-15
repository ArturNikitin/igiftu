package com.svetka.igiftu.component.board

import com.svetka.igiftu.entity.Board
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository : JpaRepository<Board, Long>