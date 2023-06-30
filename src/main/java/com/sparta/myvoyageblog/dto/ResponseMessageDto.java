package com.sparta.myvoyageblog.dto;

import lombok.Getter;

@Getter
public class ResponseMessageDto {
    private String message;
    private int status;

    public ResponseMessageDto(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
