package com.svetka.igiftu.component.board

import com.svetka.igiftu.component.wish.WishComponent
import com.svetka.igiftu.dto.BoardDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Board
import com.svetka.igiftu.entity.Wish
import javax.persistence.EntityNotFoundException
import ma.glasnost.orika.MapperFacade
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class BoardService(
	private val mapper: MapperFacade,
	private val wishService: WishComponent,
	private val boardRepository: BoardRepository
) : BoardComponent {

	private val logger = KotlinLogging.logger { }

	override fun createBoard(boardDto: BoardDto): BoardDto {
		TODO("Not yet implemented")
	}

	@Transactional
	override fun addWishes(boardId: Long, wishesDto: Set<WishDto>): BoardDto {
		logger.debug { "Attaching wishes:{${wishesDto}} to board: {$boardId}" }
		val board = getBoard(boardId)
		val wishes = connectWishesToBoard(board, wishesDto)
		board.wishes.addAll(wishes)
		val savedBoard = saveOrUpdateUser(board)
		logger.debug { "Attached wishes to $savedBoard" }
		return savedBoard
	}

	@Transactional
	override fun deleteWishes(boardId: Long, wishesDto: Set<WishDto>): BoardDto {
		logger.debug { "Removing wishes:{${wishesDto}} to board: {$boardId}" }
		val board = getBoard(boardId)
		val wishes = connectWishesToBoard(board, wishesDto)
		board.wishes.removeAll(wishes)
		val savedBoard = saveOrUpdateUser(board)
		logger.debug { "Removed wishes from $savedBoard" }
		return savedBoard
	}

	private fun getBoard(boardId: Long) =
		boardRepository.findById(boardId)
			.orElseThrow { EntityNotFoundException("Board [$boardId] not found") }

	private fun connectWishesToBoard(board: Board, wishesDto: Set<WishDto>) =
		wishService.getWishesById(wishesDto.map { it.id!! }.toSet())
			.map { mapper.map(it, Wish::class.java) }
			.onEach { it.boards.add(board) }
			.toSet()

	private fun saveOrUpdateUser(board: Board) = getBoardDto(boardRepository.save(board))

	private fun getBoardDto(board: Board) = mapper.map(board, BoardDto::class.java)
}