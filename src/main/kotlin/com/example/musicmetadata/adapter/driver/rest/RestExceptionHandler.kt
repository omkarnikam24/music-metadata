package com.example.musicmetadata.adapter.driver.rest

import com.example.musicmetadata.adapter.driven.db.exception.DatabaseException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    fun handleException(exception: IllegalArgumentException): ResponseEntity<Problem> =
        ResponseEntity.status(BAD_REQUEST)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                Problem(
                    title = BAD_REQUEST.name,
                    status = BAD_REQUEST.value(),
                ),
            ).also {
                log.error("IllegalArgumentException has been thrown: ${exception.cause}")
            }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    fun handleException(exception: DatabaseException): ResponseEntity<Problem> =
        ResponseEntity.status(INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .body(
                Problem(
                    title = INTERNAL_SERVER_ERROR.name,
                    status = INTERNAL_SERVER_ERROR.value(),
                ),
            ).also {
                log.error("DatabaseException has been thrown: ${exception.cause}")
            }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}

data class Problem(
    val title: String,
    val status: Int,
)
