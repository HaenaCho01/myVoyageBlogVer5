package com.sparta.myvoyageblog.dto;

import lombok.Getter;

@Getter
public class UserResponseDto {
    private String message;
    private int status;

    public UserResponseDto(String service, String message, int status) {
        this.message = service + "에 " + message + "하였습니다.";
        this.status = status;
    }
}
