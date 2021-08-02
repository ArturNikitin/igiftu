package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.Content
import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.service.ContentManager
import com.svetka.igiftu.service.ReaderManager
import com.svetka.igiftu.service.WishService
import com.svetka.igiftu.service.impl.ContentType.WISH
import java.security.Principal
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user/{userId}/wish")
@CrossOrigin
class WishController(
	private val contentManager: ContentManager,
	private val readerManager: ReaderManager,
	private val wishService: WishService
) {

	private val log = KotlinLogging.logger { }

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	fun getWishes(
		@PathVariable userId: Long,
		principal: Principal?
	) : PayloadDto {
		log.info { "Received request to get wishes for user $userId" }
		val payload = readerManager.getUserContent(userId, principal?.name, WISH)
		log.info { "Finished request to get wished for user $userId wish data {$payload}" }
		return payload
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	fun createWish(
		@PathVariable userId: Long,
		@RequestBody wishDto: WishDto,
		principal: Principal
	) : Content {
		log.info { "Received request to create wish for user {$userId} and data {$wishDto}" }
		val wish = contentManager.create(userId, wishDto, principal.name, wishService)
		log.info { "Finished request to create wish for user {$userId} and data {$wish}" }
		return wish
	}

	@PutMapping("/{wishId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	fun updateWish(
		@PathVariable userId: Long,
		@PathVariable wishId: Long,
		@RequestBody wishDto: WishDto,
		principal: Principal
	) : Any {
		log.info { "Received request to update wish {$wishDto} for user {$userId} and data {$wishDto}" }
		val wish = contentManager.update(userId, wishId, wishDto, principal.name, wishService)
		log.info { "Finished request to update wish {$wishId} for user {$userId} and data {$wish}" }
		return wish
	}

	@DeleteMapping("/{wishId}")
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	fun deleteWish(
		@PathVariable userId: Long,
		@PathVariable wishId: Long,
		principal: Principal
	) {
		log.info { "Received request to delete a wish with id {$wishId} and user ${principal.name}" }
		contentManager.delete(userId, wishId, principal.name, wishService)
		log.info { "Completed request to delete wish with id with id {$wishId} and user ${principal.name}" }
	}
}