package com.sparta.myvoyageblog.controller;

import com.sparta.myvoyageblog.dto.CommentRequestDto;
import com.sparta.myvoyageblog.dto.CommentResponseDto;
import com.sparta.myvoyageblog.dto.ResponseMessageDto;
import com.sparta.myvoyageblog.exception.ErrorCode;
import com.sparta.myvoyageblog.exception.GlobalExceptionHandler;
import com.sparta.myvoyageblog.security.UserDetailsImpl;
import com.sparta.myvoyageblog.service.CommentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class CommentController {
    private final CommentService commentService;
    private final GlobalExceptionHandler globalExceptionHandler;

    // 선택한 게시글에 대한 모든 댓글 조회
    @GetMapping("/{postid}/comments")
    public List<CommentResponseDto> getCommentsByPostId(@PathVariable Long postid) {
        return commentService.getCommentsByPostId(postid);
    }

    // 댓글 작성
    @PostMapping("/{postid}/comments")
    public CommentResponseDto createComment(@PathVariable Long postid, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postid, requestDto, userDetails.getUser());
    }

    // 선택한 댓글 수정
    @PutMapping("/{postid}/comments/{commentid}")
    public Object updateComment(@PathVariable Long postid, @PathVariable Long commentid, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        CommentResponseDto responseDto = commentService.updateComment(postid, commentid, requestDto, userDetails.getUser(), response);

        if (response.getStatus() == 400) {
            return globalExceptionHandler.badRequestException(ErrorCode.USER_ONLY_ERROR);
        } else if (response.getStatus() == 404) {
            return globalExceptionHandler.badRequestException(ErrorCode.NOT_FOUND_ERROR);
        } else {
            return responseDto;
        }
    }

    // 선택한 댓글 삭제
    @DeleteMapping("/{postid}/comments/{commentid}")
    public Object deleteComment(@PathVariable Long postid, @PathVariable Long commentid, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        commentService.deleteComment(postid, commentid, userDetails.getUser(), response);

        if (response.getStatus() == 400) {
            return globalExceptionHandler.badRequestException(ErrorCode.USER_ONLY_ERROR);
        } else if (response.getStatus() == 404) {
            return globalExceptionHandler.badRequestException(ErrorCode.NOT_FOUND_ERROR);
        } else {
            return new ResponseMessageDto("해당 댓글의 삭제를 완료했습니다.", response.getStatus());
        }
    }

    // 선택한 댓글 좋아요
    @PutMapping("/{postid}/comments/{commentid}/likes")
    public Object commentLike(@PathVariable Long postid, @PathVariable Long commentid, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        CommentResponseDto responseDto = commentService.commentLike(postid, commentid, userDetails.getUser(), response);

        if (response.getStatus() == 400) {
            return globalExceptionHandler.badRequestException(ErrorCode.USER_NOT_ERROR);
        } else if (response.getStatus() == 404) {
            return globalExceptionHandler.badRequestException(ErrorCode.NOT_FOUND_ERROR);
        } else {
            return responseDto;
        }
    }
}
