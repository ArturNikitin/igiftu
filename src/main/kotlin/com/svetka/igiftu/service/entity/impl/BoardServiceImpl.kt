package com.svetka.igiftu.service.entity.impl

import com.svetka.igiftu.dto.BoardDto
import com.svetka.igiftu.dto.Content
import com.svetka.igiftu.dto.ImageDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.repository.BoardRepository
import com.svetka.igiftu.service.entity.BoardService
import com.svetka.igiftu.service.entity.ImageService
import com.svetka.igiftu.service.entity.WishService
import com.svetka.igiftu.service.manager.impl.OwnerType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class BoardServiceImpl(
	val imageService: ImageService,
	val wishService: WishService,
	val boardRepository: BoardRepository
) : BoardService {
	override fun prepare(content: Content): Content {
		return prepareBoardForCreation(content as BoardDto)
	}

	override fun delete(contentId: Long) {
		TODO("Not yet implemented")
	}

	override fun update(contentId: Long, content: Content): Content {
		TODO("Not yet implemented")
	}

	override fun isOwner(ownerId: Long, contentId: Long?) =
		boardRepository.findById(ownerId).orElseThrow { EntityNotFoundException("Board [$ownerId]") }
			.user?.id == contentId!!

	override fun get(ownerId: Long, ownerType: OwnerType): List<Content> {
		TODO("Not yet implemented")
	}

	override fun addContent(ownerId: Long, content: Content): Content {
		TODO("Not yet implemented")
	}

	private fun prepareBoardForCreation(requestBoard: BoardDto): Content {
		return BoardDto(
			name = requestBoard.name,
			createdDate = LocalDateTime.now().format(format),
			image = requestBoard.image?.content?.let { processCustomImage(it) } ?: getDefaultImage()
		).apply {
			lastModifiedDate = createdDate
//			wishes = getWishesById(requestBoard.wishes.map { it.id!! }).toSet()
		}
	}

	private fun getWishesById(wishes: List<Long>) =
		wishService.getWishes(wishes)

	private fun processCustomImage(content: ByteArray) =
		imageService.saveImage(content)

	private fun getDefaultImage(): ImageDto =
		imageService.getDefaultImage(DEFAULT_BOARD_IMAGE_NAME)

	private val format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
}

private const val DEFAULT_BOARD_IMAGE_NAME = "default-board"