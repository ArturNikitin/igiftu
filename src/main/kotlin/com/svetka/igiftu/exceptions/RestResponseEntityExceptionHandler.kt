package com.svetka.igiftu.exceptions

import java.util.HashMap
import java.util.function.Consumer
import javax.persistence.EntityExistsException
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
            errors["message"] = errorMessage
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
        HttpHeaders.EMPTY, HttpStatus.NOT_FOUND, request
    )

    @ExceptionHandler(
        value = [EntityExistsException::class]
    )
    fun handleEntityExists(
        ex: EntityExistsException,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errors: MutableMap<String, String?> = HashMap()
        errors["message"] = ex.message
        return handleExceptionInternal(
            ex, errors,
            HttpHeaders.EMPTY, HttpStatus.NOT_FOUND, request
        )
    }

    @ExceptionHandler(
            value = [SecurityException::class]
    )
    fun handleEntityExists(
        ex: SecurityException,
        request: WebRequest
    ): ResponseEntity<Any> = handleExceptionInternal(
            ex, ex.message,
            HttpHeaders.EMPTY, HttpStatus.UNAUTHORIZED, request
    )

}