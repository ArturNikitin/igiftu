package com.svetka.igiftu.controller

import com.svetka.igiftu.component.wish.WishComponent
import com.svetka.igiftu.dto.Content
import com.svetka.igiftu.dto.PayloadDto
import com.svetka.igiftu.dto.UserInfo
import com.svetka.igiftu.dto.WishDto
import com.svetka.igiftu.service.ReaderManager
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
	private val readerManager: ReaderManager,
	private val wishService: WishComponent,
) {

	private val log = KotlinLogging.logger { }

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	fun getWishes(
		@PathVariable userId: Long,
		principal: Principal?
	): PayloadDto {
		log.info { "Received request from ${principal?.name ?: "unauthorized user"} to get wishes for user [$userId]" }
		val payload = readerManager.readWishesByUser(userId, principal?.name)
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
	): Content {
		log.info { "Received request to create wish for user {$userId} and data {$wishDto}" }
		val wish = wishService.createWish(UserInfo(userId, principal.name), wishDto)
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
	): WishDto {
		log.info { "Received request to update wish {$wishDto} for user {$userId} and data {$wishDto}" }
		val wish = wishService.updateWish(UserInfo(userId, principal.name), wishDto)
		log.info { "Finished request to update wish {$wishId} for user {$userId} and data {$wish}" }
		return wish
	}

	@DeleteMapping("/{wishId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	fun deleteWish(
		@PathVariable userId: Long,
		@PathVariable wishId: Long,
		principal: Principal
	) {
		log.info { "Received request to delete a wish with id {$wishId} and user ${principal.name}" }
		wishService.deleteWish(UserInfo(userId, principal.name), wishId)
		log.info { "Completed request to delete wish {$wishId} and user ${principal.name}" }
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
	fun deleteWishes(
		@PathVariable userId: Long,
		@RequestBody wishes: Set<WishDto>,
		principal: Principal
	) {
		log.info { "Received request to delete a wish with ids {${wishes.map { it.id }}} and user ${principal.name}" }
		wishService.deleteWishes(UserInfo(userId, principal.name), wishes)
		log.info { "Completed request to delete wishes {${wishes.map { it.id }}} and user ${principal.name}" }
	}
}