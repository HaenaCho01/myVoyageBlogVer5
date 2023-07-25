package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.CommentRequestDto;
import com.sparta.myvoyageblog.dto.CommentResponseDto;
import com.sparta.myvoyageblog.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

public interface CommentService {
	/**
	 * 선택한 게시글에 대한 전체 댓글 목록 조회
	 * @param postId 게시글 번호
	 * @return 선택한 게시글에 대한 전체 댓글 목록
	 */
	List<CommentResponseDto> getCommentsByPostId(Long postId);

	/**
	 * 댓글 생성
	 * @param postId 게시글 번호
	 * @param requestDto 댓글 생성 요청정보
	 * @param user 댓글 생성 요청자
	 * @return 댓글 생성 결과
	 */
	CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user);

	/**
	 * 댓글 수정
	 * @param postId 게시글 번호
	 * @param commentId 댓글 번호
	 * @param requestDto 댓글 수정 요청정보
	 * @param user 댓글 수정 요청자
	 * @return 댓글 수정 결과
	 */
	CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto requestDto, User user);

	/**
	 * 댓글 삭제
	 * @param postId 게시글 번호
	 * @param commentId 댓글 번호
	 * @param user 댓글 삭제 요청자
	 */
	void deleteComment(Long postId, Long commentId, @AuthenticationPrincipal User user);

	/**
	 * 댓글 좋아요 추가
	 * @param postId 게시글 번호
	 * @param commentId 댓글 번호
	 * @param user 댓글 좋아요 추가 요청자
	 */
	void commentInsertLike(Long postId, Long commentId, User user);

	/**
	 * 댓글 좋아요 취소
	 * @param postId 게시글 번호
	 * @param commentId 댓글 번호
	 * @param user 댓글 좋아요 취소 요청자
	 */
	void commentDeleteLike(Long postId, Long commentId, User user);
}
