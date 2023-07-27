package com.sparta.myvoyageblog.controller;

import com.sparta.myvoyageblog.dto.ApiResponseDto;
import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.dto.PostResponseDto;
import com.sparta.myvoyageblog.security.UserDetailsImpl;
import com.sparta.myvoyageblog.service.PostServiceImpl;
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
    private final PostServiceImpl postServiceImpl;

    // 게시글 작성
    @PostMapping("/posts")
    public ResponseEntity<ApiResponseDto> createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(postServiceImpl.createPost(requestDto, userDetails.getUser()));
    }

    // 전체 게시글 목록 조회
    @GetMapping("/posts")
    public ResponseEntity<List<List<Object>>> getPosts() {
        return ResponseEntity.ok().body(postServiceImpl.getPosts());
    }

    // 선택한 게시글 조회
    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<Object>> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok().body(postServiceImpl.getPostById(postId));
    }

    // 키워드 검색으로 게시글 조회
    @GetMapping("/posts/search")
    public ResponseEntity<List<PostResponseDto>> searchPostsByKeyword(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok().body(postServiceImpl.searchPostsByKeyword(keyword));
    }

    // 선택한 게시글 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        PostResponseDto result = postServiceImpl.updatePost(postId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(result);
    }

    // 선택한 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponseDto> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postServiceImpl.deletePost(postId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("해당 게시글의 삭제를 완료했습니다.", HttpStatus.OK.value()));
    }

    // 선택한 게시글 좋아요 추가
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<ApiResponseDto> postInsertLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postServiceImpl.postInsertLike(postId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("해당 게시글에 좋아요가 추가되었습니다.", HttpStatus.OK.value()));
    }

    // 선택한 게시글 좋아요 취소
    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<ApiResponseDto> postDeleteLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postServiceImpl.postDeleteLike(postId, userDetails.getUser());
        return ResponseEntity.ok().body(new ApiResponseDto("해당 게시글에 좋아요가 취소되었습니다.", HttpStatus.OK.value()));
    }
}
