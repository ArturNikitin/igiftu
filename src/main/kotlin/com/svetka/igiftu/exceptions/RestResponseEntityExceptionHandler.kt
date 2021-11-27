package com.svetka.igiftu.exceptions

import java.util.function.Consumer
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.LinkedMultiValueMap
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
			val errorMessage = error.defaultMessage
			errors[fieldName] = errorMessage
		})
		errors["message"] = "Какие-то поля отсутвтвуют или неккоректны"
		return handleExceptionInternal(
			ex, errors, HttpHeaders.EMPTY,
			HttpStatus.BAD_REQUEST, request
		)
	}

	@ExceptionHandler(value = [EntityNotFoundException::class])
	fun handleEntityNotFound(ex: EntityNotFoundException, request: WebRequest) = prepareResponse(
		ex, mapOf("message" to (ex.message ?: "")),
		HttpStatus.NOT_FOUND, request
	)

	@ExceptionHandler(
		value = [EntityExistsException::class]
	)
	fun handleEntityExists(ex: EntityExistsException, request: WebRequest) = prepareResponse(
		ex, mapOf("message" to (ex.message ?: "")),
		HttpStatus.NOT_FOUND, request
	)

	@ExceptionHandler(value = [IllegalStateException::class])
	fun handleIllegalStateException(ex: IllegalStateException, request: WebRequest) = prepareResponse(
		ex, mapOf("message" to (ex.message ?: "")),
		HttpStatus.NOT_FOUND, request
	)

	@ExceptionHandler(value = [SecurityException::class])
	fun handleSecurityException(ex: SecurityException, request: WebRequest) = prepareResponse(
		ex, mapOf("message" to (ex.message ?: "")),
		HttpStatus.UNAUTHORIZED, request
	)

	private fun prepareResponse(
		ex: Exception,
		json: Map<String, String>,
		status: HttpStatus,
		request: WebRequest
	) = handleExceptionInternal(
		ex, json, HttpHeaders(
			LinkedMultiValueMap<String, String>()
				.apply { add("Content-Type", "application/json") }),
		status, request
	)

}