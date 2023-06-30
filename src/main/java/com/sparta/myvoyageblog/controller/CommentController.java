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
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class CommentController {
    private final CommentService commentService;
    private final ResponseMessageUtil responseMessageUtil;

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
    public CommentResponseDto updateComment(@PathVariable Long postid, @PathVariable Long commentid, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) throws IOException {
        CommentResponseDto responseDto = commentService.updateComment(postid, commentid, requestDto, userDetails.getUser(), response);

        if (response.getStatus() == 400) {
            responseMessageUtil.statusResponse(response, "작성자만 수정할 수 있습니다.");
            return null;
        } else if (response.getStatus() == 404) {
            responseMessageUtil.statusResponse(response, "주소가 잘못되어 해당 댓글을 찾을 수 없습니다.");
            return null;
        } else {
            return responseDto;
        }
    }

    // 선택한 댓글 삭제
    @DeleteMapping("/{postid}/comments/{commentid}")
    public void deleteComment(@PathVariable Long postid, @PathVariable Long commentid, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) throws IOException {
        commentService.deleteComment(postid, commentid, userDetails.getUser(), response);

        if (response.getStatus() == 400) {
            responseMessageUtil.statusResponse(response, "작성자만 삭제할 수 있습니다.");
        } else if (response.getStatus() == 404) {
            responseMessageUtil.statusResponse(response, "주소가 잘못되어 해당 댓글을 찾을 수 없습니다.");
        } else {
            responseMessageUtil.statusResponse(response, "해당 댓글의 삭제를 완료하였습니다.");
        }
    }

    // 선택한 댓글 좋아요
    @PutMapping("/{postid}/comments/{commentid}/likes")
    public CommentResponseDto commentLike(@PathVariable Long postid, @PathVariable Long commentid, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) throws IOException {
        if (response.getStatus() == 400) {
            responseMessageUtil.statusResponse(response, "본인이 작성한 댓글에는 좋아요를 누를 수 없습니다.");
            return null;
        } else if (response.getStatus() == 404) {
            responseMessageUtil.statusResponse(response, "주소가 잘못되어 해당 댓글을 찾을 수 없습니다.");
            return null;
        } else {
            CommentResponseDto responseDto = commentService.commentLike(postid, commentid, userDetails.getUser(), response);
            return responseDto;
        }
    }
}
