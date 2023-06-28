package com.sparta.myvoyageblog.dto;

import lombok.Getter;

@Getter
public class UserResponseDto {
    private String message;
    private int status;

    public UserResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
    }
}
