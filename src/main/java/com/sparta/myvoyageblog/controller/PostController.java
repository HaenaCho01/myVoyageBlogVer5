package com.sparta.myvoyageblog.controller;

import com.sparta.myvoyageblog.dto.ApiResponseDto;
import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.dto.PostResponseDto;
import com.sparta.myvoyageblog.exception.ErrorCode;
import com.sparta.myvoyageblog.exception.GlobalExceptionHandler;
import com.sparta.myvoyageblog.security.UserDetailsImpl;
import com.sparta.myvoyageblog.service.PostService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

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

    // 선택한 댓글 좋아요 추가
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<ApiResponseDto> postInsertLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 오류가 나지 않을 경우 해당 댓글 좋아요 추가
        try {
            PostResponseDto responseDto = postService.postInsertLike(postId, userDetails.getUser());
            return ResponseEntity.ok().body(responseDto);
        }
        // 게시글이 존재하지 않을 경우 오류 메시지 반환
        catch (EntityNotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto(notFoundException.getMessage(), HttpStatus.NOT_FOUND.value()));
        }
        // 작성한 유저가 좋아요를 시도할 경우 오류 메시지 반환
        catch (AccessDeniedException accessDeniedException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto(accessDeniedException.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        // 사용자가 이미 좋아요를 누른 경우 오류 메시지 반환
        catch (DataIntegrityViolationException dataIntegrityViolationException) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponseDto(dataIntegrityViolationException.getMessage(), HttpStatus.CONFLICT.value()));
        }
    }

    // 선택한 댓글 좋아요 취소
    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<ApiResponseDto> postDeleteLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        // 오류가 나지 않을 경우 해당 댓글 좋아요 취소
        try {
            PostResponseDto responseDto = postService.postDeleteLike(postId, userDetails.getUser());
            return ResponseEntity.ok().body(responseDto);
        }
        // 게시글이 존재하지 않을 경우 오류 메시지 반환
        catch (EntityNotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponseDto(notFoundException.getMessage(), HttpStatus.NOT_FOUND.value()));
        }
        // 작성한 유저가 좋아요를 시도할 경우 오류 메시지 반환
        catch (AccessDeniedException accessDeniedException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponseDto(accessDeniedException.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
        // 사용자가 좋아요를 누른 적이 없는 경우 오류 메시지 반환
        catch (NoSuchElementException noSuchElementException) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponseDto(noSuchElementException.getMessage(), HttpStatus.CONFLICT.value()));
        }
    }
}
