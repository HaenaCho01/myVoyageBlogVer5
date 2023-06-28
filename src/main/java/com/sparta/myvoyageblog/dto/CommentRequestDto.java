package com.sparta.myvoyageblog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    private Long postId;

    @NotBlank
    private String comment;
}
