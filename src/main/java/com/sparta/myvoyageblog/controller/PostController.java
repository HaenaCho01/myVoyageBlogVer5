package com.sparta.myvoyageblog.controller;

import com.sparta.myvoyageblog.dto.ApiResponseDto;
import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.dto.PostResponseDto;
import com.sparta.myvoyageblog.security.UserDetailsImpl;
import com.sparta.myvoyageblog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;

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
    public ResponseEntity<ApiResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto result = postService.updatePost(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    // 선택한 게시글 삭제
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(id, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("해당 게시글의 삭제를 완료했습니다.", HttpStatus.OK.value()));
    }

    // 선택한 게시글 좋아요 추가
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<ApiResponseDto> postInsertLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.postInsertLike(postId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("해당 게시글에 좋아요가 추가되었습니다.", HttpStatus.OK.value()));
    }

    // 선택한 게시글 좋아요 취소
    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<ApiResponseDto> postDeleteLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.postDeleteLike(postId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("해당 게시글에 좋아요가 취소되었습니다.", HttpStatus.OK.value()));
    }
}
