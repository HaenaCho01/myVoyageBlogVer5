package com.sparta.myvoyageblog.dto;

import com.sparta.myvoyageblog.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto extends ApiResponseDto {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private long likeCnt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.username = comment.getUser().getUsername();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
        this.likeCnt = comment.getLikeCnt();
    }
}
