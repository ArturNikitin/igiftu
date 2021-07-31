package com.svetka.igiftu.controller

import com.svetka.igiftu.dto.Content
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.service.ContentCreationManager
import com.svetka.igiftu.service.impl.ContentType
import java.security.Principal
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
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
	private val contentCreationManager: ContentCreationManager
) {

	private val log = KotlinLogging.logger { }

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	fun createWish(
		@PathVariable userId: Long,
		@RequestBody wishDto: WishDto,
		principal: Principal
	)
			: Content {
		log.info { "Received request to create wish for user {$userId} and data {$wishDto}" }
		val wish = contentCreationManager.create(userId, wishDto, principal.name)
		log.info { "Finished request to create wish for user {$userId} and data {$wish}" }
		return wish
	}

	@PutMapping("/{wishId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	fun updateWish(
		@PathVariable userId: Long,
		@PathVariable wishId: Long,
		@RequestBody wishDto: WishDto,
		principal: Principal
	)
			: Any {
		log.info { "Received request to update wish {$wishDto} for user {$userId} and data {$wishDto}" }
		val wish = contentCreationManager.update(userId, wishId, wishDto, principal.name)
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
		contentCreationManager.delete(userId, wishId, ContentType.WISH, principal.name)
		log.info { "Completed request to delete wish with id with id {$wishId} and user ${principal.name}" }
	}
}