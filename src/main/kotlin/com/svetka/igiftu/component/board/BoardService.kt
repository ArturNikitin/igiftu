package com.svetka.igiftu.component.board

import com.svetka.igiftu.aop.ModificationPermissionRequired
import com.svetka.igiftu.component.user.UserComponent
import com.svetka.igiftu.component.wish.WishComponent
import com.svetka.igiftu.dateTimeFormat
import com.svetka.igiftu.dto.BoardDto
import com.svetka.igiftu.dto.ImageDto
import com.svetka.igiftu.dto.UserInfo
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.entity.Board
import com.svetka.igiftu.entity.Image
import com.svetka.igiftu.entity.Wish
import com.svetka.igiftu.exceptions.SecurityCreationException
import com.svetka.igiftu.exceptions.SecurityModificationException
import com.svetka.igiftu.service.ImageService
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException
import ma.glasnost.orika.MapperFacade
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
internal class BoardService(
	private val mapper: MapperFacade,
	private val wishService: WishComponent,
	private val userService: UserComponent,
	private val imageService: ImageService,
	private val boardRepository: BoardRepository
) : BoardComponent {

	private val logger = KotlinLogging.logger { }

	@Transactional
	@ModificationPermissionRequired
	override fun createBoard(boardDto: BoardDto, userInfo: UserInfo): BoardDto {
		if (!isOperationAllowed(userInfo.id, userInfo.username))
			throw SecurityCreationException("illegal creation attempt by user [${userInfo.username}]")

		boardDto.apply {
			createdDate = LocalDateTime.now().format(dateTimeFormat)
			lastModifiedDate = createdDate
		}
		val board = mapper.map(boardDto, Board::class.java)
			.apply { image = boardDto.image?.let(::dealWithImage) }

		return userService.addBoards(userInfo.id, setOf(board))
			.filter { it.lastModifiedDate == boardDto.lastModifiedDate }
			.map { mapper.map(it, BoardDto::class.java) }
			.first()
	}

	@Transactional
	@ModificationPermissionRequired
	override fun addWishes(boardId: Long, wishesDto: Set<WishDto>, userInfo: UserInfo): BoardDto {
		logger.debug { "Attaching wishes:{${wishesDto}} to board: {$boardId}" }
		val board = getBoardIfExists(boardId)
		val wishes = connectWishesToBoard(board, wishesDto)
		board.wishes.addAll(wishes)

		val savedBoard = boardRepository.save(board).also { it.wishes }
		val result = getBoardDto(savedBoard)
		logger.debug { "Attached wishes to $result" }
		return result
	}

	@Transactional
	@ModificationPermissionRequired
	override fun deleteWishes(boardId: Long, wishesDto: Set<WishDto>, userInfo: UserInfo): BoardDto {
		logger.debug { "Removing wishes:{${wishesDto}} to board: {$boardId}" }
		val board = getBoardIfExists(boardId)
		val wishes = connectWishesToBoard(board, wishesDto)
		board.wishes.removeAll(wishes)
		val savedBoard = saveOrUpdateBoard(board)
		logger.debug { "Removed wishes from $savedBoard" }
		return savedBoard
	}

	@Transactional
	override fun deleteBoard(boardId: Long, userInfo: UserInfo) {
		getBoardIfExists(boardId)
		boardRepository.deleteById(boardId)
		logger.debug { "Board $boardId deleted" }
	}

	private fun getBoardIfExists(boardId: Long) =
		boardRepository.findById(boardId)
			.orElseThrow { EntityNotFoundException("Board [$boardId] not found") }

	private fun connectWishesToBoard(board: Board, wishesDto: Set<WishDto>) =
		wishService.getWishesById(wishesDto.map { it.id!! }.toSet())
			.map { mapper.map(it, Wish::class.java) }
			.onEach { it.boards.add(board) }
			.toSet()

	private fun saveOrUpdateBoard(board: Board) = getBoardDto(boardRepository.save(board))

	private fun getBoardDto(board: Board) = mapper.map(board, BoardDto::class.java)

	private fun processCustomImage(content: ByteArray) =
		imageService.uploadImage(content)

	private fun isOperationAllowed(userId: Long, username: String) =
		userService.isSameUser(userId, username)

	private fun getDefaultImage(): ImageDto =
		imageService.getDefaultImage(DEFAULT_BOARD_IMAGE_NAME)

	private fun dealWithImage(imageDto: ImageDto): Image =
		imageService.uploadImage(imageDto.content!!)
			.let { mapper.map(it, Image::class.java) }
}

private const val DEFAULT_BOARD_IMAGE_NAME = "default-board"