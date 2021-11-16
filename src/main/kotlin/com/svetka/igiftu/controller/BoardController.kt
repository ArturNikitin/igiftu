package com.svetka.igiftu.controller

import com.svetka.igiftu.component.board.BoardComponent
import com.svetka.igiftu.component.board.UserInfo
import com.svetka.igiftu.dto.BoardDto
import com.svetka.igiftu.dto.Content
import com.svetka.igiftu.service.entity.BoardService
import com.svetka.igiftu.service.manager.ContentManager
import com.svetka.igiftu.service.manager.ReaderManager
import java.security.Principal
import mu.KotlinLogging
import org.springframework.http.HttpStatus.ACCEPTED
import org.springframework.http.HttpStatus.CREATED
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user/{userId}/board")
class BoardController(
	private val contentManager: ContentManager,
	private val readerManager: ReaderManager,
	private val boardService: BoardService,
	private val boardComponent: BoardComponent
) {

	private val log = KotlinLogging.logger { }

	@PostMapping
	@ResponseStatus(CREATED)
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	fun createBoard(
		principal: Principal,
		@PathVariable userId: Long,
		@RequestBody incomingBoard: BoardDto
	): Content {
		log.info { "Receive request to create board for user [$userId] and data {$incomingBoard}" }
//		val createdBoard = contentManager.create(userId, incomingBoard, principal.name, boardService)
		val createdBoard = boardComponent.createBoard(incomingBoard, UserInfo(userId, principal.name))
		log.info { "Finished request to create board for user [$userId] and data {$createdBoard}" }
		return createdBoard
	}

	@PatchMapping("/{boardId}/wishes")
	@ResponseStatus(ACCEPTED)
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	fun addWishes(
		principal: Principal,
		@PathVariable userId: Long,
		@PathVariable boardId: Long,
		@RequestBody incomingBoard: BoardDto
	) : Content {
		log.info { "Received request to add wishes {${incomingBoard.wishes}} to board [$boardId] for user [$userId]" }
		val board = boardComponent.addWishes(boardId, incomingBoard.wishes)
		log.info { "Finished request to add wishes {${incomingBoard.wishes}} to board [$boardId] for user [$userId]" }
		return board
	}

	@DeleteMapping("/{boardId}/wishes")
	@ResponseStatus(ACCEPTED)
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	fun deleteWishes(
		principal: Principal,
		@PathVariable userId: Long,
		@PathVariable boardId: Long,
		@RequestBody incomingBoard: BoardDto
	) : Content {
		log.info { "Received request to add wishes {${incomingBoard.wishes}} to board [$boardId] for user [$userId]" }
		val board = boardComponent.deleteWishes(boardId, incomingBoard.wishes)
		log.info { "Finished request to add wishes {${incomingBoard.wishes}} to board [$boardId] for user [$userId]" }
		return board
	}

}