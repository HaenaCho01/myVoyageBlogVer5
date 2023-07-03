package com.sparta.myvoyageblog.exception;

import com.sparta.myvoyageblog.dto.ResponseMessageDto;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Component // @RestControllerAdvice 사용법을 아직 잘 모르겠음..
public class GlobalExceptionHandler {

	// 에러 처리
	@ExceptionHandler
	public ResponseMessageDto badRequestException(ErrorCode errorCode) {
		return new ResponseMessageDto(errorCode.getMessage(), errorCode.getStatus().value());
	}
}