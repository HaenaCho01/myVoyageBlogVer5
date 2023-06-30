package com.sparta.myvoyageblog.controller;

import com.sparta.myvoyageblog.dto.CommentRequestDto;
import com.sparta.myvoyageblog.dto.CommentResponseDto;
import com.sparta.myvoyageblog.util.ResponseMessageUtil;
import com.sparta.myvoyageblog.security.UserDetailsImpl;
import com.sparta.myvoyageblog.service.CommentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;
    private final ResponseMessageUtil responseMessageUtil;

    // 댓글 작성
    @PostMapping("/comments")
    public CommentResponseDto createComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(requestDto, userDetails.getUser());
    }

    // 선택한 댓글 수정
    @PutMapping("/comments/{id}")
    public CommentResponseDto updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) throws IOException {
        CommentResponseDto responseDto = commentService.updateComment(id, requestDto, userDetails.getUser(), response);

        if (response.getStatus() == 400) {
            responseMessageUtil.statusResponse(response, "작성자만 수정할 수 있습니다.");
            return null;
        } else {
            return responseDto;
        }
    }

    // 선택한 댓글 삭제
    @DeleteMapping("/comments/{id}")
    public void deleteComment(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) throws IOException {
        commentService.deleteComment(id, userDetails.getUser(), response);

        if (response.getStatus() == 400) {
            responseMessageUtil.statusResponse(response, "작성자만 삭제할 수 있습니다.");
        } else {
            responseMessageUtil.statusResponse(response, "해당 댓글의 삭제를 완료하였습니다.");
        }
    }

    // 선택한 댓글 좋아요
    @PutMapping("/comments/{id}/likes")
    public CommentResponseDto commentLike(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) throws IOException {
        CommentResponseDto responseDto = commentService.commentLike(id, userDetails.getUser(), response);

        return responseDto;
    }
}
