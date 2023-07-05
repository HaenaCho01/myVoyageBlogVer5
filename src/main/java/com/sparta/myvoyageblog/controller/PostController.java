package com.sparta.myvoyageblog.controller;

import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.dto.PostResponseDto;
import com.sparta.myvoyageblog.dto.ApiResponseDto;
import com.sparta.myvoyageblog.exception.ErrorCode;
import com.sparta.myvoyageblog.exception.GlobalExceptionHandler;
import com.sparta.myvoyageblog.security.UserDetailsImpl;
import com.sparta.myvoyageblog.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;
    private final GlobalExceptionHandler globalExceptionHandler;

    // 게시글 작성
    @PostMapping("/posts")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(requestDto, userDetails.getUser());
    }

    // 전체 게시글 목록 조회
    @GetMapping("/posts")
    public List<List<Object>> getPosts() {
        return postService.getPosts();
    }

    // 선택한 게시글 조회
    @GetMapping("/posts/{id}")
    public List<Object> getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    // 선택한 게시글 수정
    @PutMapping("/posts/{id}")
    public Object updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) throws IOException {
        PostResponseDto responseDto = postService.updatePost(id, requestDto, userDetails.getUser(), response);

        if (response.getStatus() == 400) {
            return globalExceptionHandler.badRequestException(ErrorCode.USER_ONLY_ERROR);
        } else {
            return responseDto;
        }
    }

    // 선택한 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public Object deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) throws IOException {
        postService.deletePost(id, userDetails.getUser(), response);

        if (response.getStatus() == 400) {
            return globalExceptionHandler.badRequestException(ErrorCode.USER_ONLY_ERROR);
        } else {
            return new ApiResponseDto( "해당 게시글의 삭제를 완료했습니다.", response.getStatus());
        }
    }
}
