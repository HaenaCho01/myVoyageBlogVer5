package com.sparta.myvoyageblog.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
	USER_ONLY_ERROR(HttpStatus.BAD_REQUEST, "작성자만 삭제/수정할 수 있습니다."),
	USER_NOT_ERROR(HttpStatus.BAD_REQUEST, "작성자는 좋아요를 누를 수 없습니다."),
	NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "주소가 잘못되어 해당 페이지를 찾을 수 없습니다."),
	LIKE_PRESENT(HttpStatus.CONFLICT, "좋아요를 이미 누르셨습니다."),
	LIKE_EMPTY(HttpStatus.CONFLICT, "좋아요를 누르시지 않았습니다.");
	private final HttpStatus status;
	private final String message;


	public HttpStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
