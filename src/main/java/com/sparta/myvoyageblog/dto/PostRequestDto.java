package com.sparta.myvoyageblog.dto;

import lombok.Getter;

@Getter
public class PostRequestDto {
    private String password;
    private String title;
    private String username;
    private String contents;
}
