package com.svetka.igiftu.exceptions

import java.util.*
import java.util.function.Consumer
import javax.persistence.EntityNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
	
	override fun handleMethodArgumentNotValid(
		ex: MethodArgumentNotValidException,
		headers: HttpHeaders,
		status: HttpStatus,
		request: WebRequest
	): ResponseEntity<Any> {
		val errors: MutableMap<String, String?> = HashMap()
		ex.bindingResult.allErrors.forEach(Consumer { error: ObjectError ->
			val fieldName = (error as FieldError).field
			val errorMessage = error.getDefaultMessage()
			errors[fieldName] = errorMessage
		})
		return handleExceptionInternal(
			ex, errors, HttpHeaders.EMPTY,
			HttpStatus.BAD_REQUEST, request
		)
	}
	
	@ExceptionHandler(
		value = [EntityNotFoundException::class]
	)
	fun handleEntityNotFound(
		ex: EntityNotFoundException,
		request: WebRequest
	): ResponseEntity<Any> = handleExceptionInternal(
		ex, ex.message,
		HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST, request
	)
	
}