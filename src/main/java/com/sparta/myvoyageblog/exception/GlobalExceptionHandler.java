package com.sparta.myvoyageblog.exception;

import com.sparta.myvoyageblog.dto.ApiResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	// 에러 처리
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ApiResponseDto> handleEntityNotFoundException(EntityNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new ApiResponseDto(ex.getMessage(), HttpStatus.NOT_FOUND.value()));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResponseDto> handleIllegalArgumentException(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ApiResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
	}
}