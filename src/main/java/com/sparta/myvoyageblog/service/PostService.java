package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.dto.PostResponseDto;
import com.sparta.myvoyageblog.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface PostService {
	/**
	 * 게시글 생성
	 * @param requestDto 게시글 생성 요청정보
	 * @param user 게시글 생성 요청자
	 * @return 게시글 생성 결과
	 */
	PostResponseDto createPost(PostRequestDto requestDto, User user);

	/**
	 * 전체 게시글 및 댓글 목록 조회
	 * @return 전체 게시글 및 댓글 목록
	 */
	List<List<Object>> getPosts();

	/**
	 * 선택한 게시글 및 댓글 목록 조회
	 * @param id 게시글 번호
	 * @return 선택한 게시글 및 댓글 목록
	 */
	List<Object> getPostById(Long id);

	/**
	 * 선택한 게시글 수정
	 * @param id 게시글 번호
	 * @param requestDto 게시글 수정 요청정보
	 * @param user 게시글 수정 요청자
	 * @return 선택한 게시글 수정 결과
	 */
	PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user);

	/**
	 * 선택한 게시글 삭제
	 * @param id 게시글 번호
	 * @param user 게시글 수정 요청자
	 */
	void deletePost(Long id, @AuthenticationPrincipal User user);

	/**
	 * 선택한 게시글 좋아요 추가
	 * @param postId 게시글 번호
	 * @param user 게시글 좋아요 추가 요청자
	 */
	void postInsertLike(Long postId, User user);

	/**
	 * 선택한 게시글 좋아요 취소
	 * @param postId 게시글 번호
	 * @param user 게시글 좋아요 취소 요청자
	 */
	void postDeleteLike(Long postId, User user);
}
