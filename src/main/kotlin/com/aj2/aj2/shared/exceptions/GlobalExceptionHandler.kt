package com.aj2.aj2.shared.exceptions

import com.aj2.aj2.shared.ApiResponse
import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(exception: NotFoundException): ResponseEntity<ApiResponse> {
        log.warn("Not found: {}", exception.message)
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(ApiResponse(message = exception.message))
    }

    // Re-thrown so it bypasses MVC exception resolution entirely and reaches Spring
    // Security's ExceptionTranslationFilter, which routes anonymous-and-denied requests
    // to the 401 entry point and authenticated-and-denied requests to the 403 handler.
    // Without this, the generic Exception::class handler below would catch it first
    // (AccessDeniedException is an Exception) and collapse both cases into a 500.
    @ExceptionHandler(AccessDeniedException::class)
    fun rethrowAccessDenied(exception: AccessDeniedException): Nothing {
        throw exception
    }

    @ExceptionHandler(ForbiddenException::class)
    fun handleForbidden(exception: ForbiddenException): ResponseEntity<ApiResponse> {
        log.warn("Forbidden: {}", exception.message)
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(ApiResponse(message = exception.message))
    }

    @ExceptionHandler(BadRequestException::class, ConstraintViolationException::class, IllegalArgumentException::class)
    fun handleBadRequest(exception: Exception): ResponseEntity<ApiResponse> {
        log.warn("Bad request: {}", exception.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse(message = exception.message))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMalformedBody(exception: HttpMessageNotReadableException): ResponseEntity<ApiResponse> {
        log.warn("Malformed request body: {}", exception.message)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse(message = "Malformed or missing required fields in request body"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(exception: MethodArgumentNotValidException): ResponseEntity<ApiResponse> {
        val errors = exception.bindingResult.fieldErrors.map { error ->
            mapOf("field" to error.field, "message" to error.defaultMessage)
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ApiResponse(message = "Validation failed", data = errors))
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpected(exception: Exception): ResponseEntity<ApiResponse> {
        log.error("Unexpected error", exception)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse(message = exception.message ?: "Internal server error"))
    }
}
