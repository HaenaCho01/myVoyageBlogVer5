package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.CommentRequestDto;
import com.sparta.myvoyageblog.dto.CommentResponseDto;
import com.sparta.myvoyageblog.entity.Comment;
import com.sparta.myvoyageblog.entity.CommentLike;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.exception.NotFoundException;
import com.sparta.myvoyageblog.exception.annotation.CommentCheckPostId;
import com.sparta.myvoyageblog.exception.annotation.CommentCheckUserNotEquals;
import com.sparta.myvoyageblog.repository.CommentLikeRepository;
import com.sparta.myvoyageblog.repository.CommentRepository;
import com.sparta.myvoyageblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
	private final CommentLikeRepository commentLikeRepository;

    // 선택한 게시글에 대한 댓글 전체 조회
	@Override
	@Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
	    return commentRepository.findAllByPostOrderByCreatedAtDesc(findPost(postId)).stream().map(CommentResponseDto::new).toList();
    }

	// 댓글 작성
	@Override
	@Transactional
    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
		Post post = findPost(postId);
        Comment comment = new Comment(post, requestDto, user);
        Comment saveComment = commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(saveComment);
        return commentResponseDto;
    }

	// 선택한 댓글 수정
	@Override
    @Transactional
	@CommentCheckPostId // postId 받은 것과 comment DB에 저장된 postId가 다를 경우 예외 처리
	@CommentCheckUserNotEquals // 다른 유저가 수정을 시도할 경우 예외 처리
    public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto requestDto, User user) {
		findComment(commentId).update(requestDto);
		CommentResponseDto commentResponseDto = new CommentResponseDto(findComment(commentId));
		return commentResponseDto;
    }

	// 선택한 댓글 삭제
	@Override
	@Transactional
	@CommentCheckPostId // postId 받은 것과 comment DB에 저장된 postId가 다를 경우 예외 처리
	@CommentCheckUserNotEquals // 다른 유저가 삭제를 시도할 경우 예외 처리
	public void deleteComment(Long postId, Long commentId, User user) {
		commentRepository.delete(findComment(commentId));
	}

	// 선택한 댓글 좋아요 기능 추가
	@Override
	@Transactional
	@CommentCheckPostId // postId 받은 것과 comment DB에 저장된 postId가 다를 경우 예외 처리
	public void commentInsertLike(Long postId, Long commentId, User user) {
		Comment comment = findComment(commentId);
		// 좋아요를 이미 누른 경우 오류 코드 반환
		if (findCommentLike(user, comment) != null) {
			throw new IllegalArgumentException("좋아요를 이미 누르셨습니다.");
		}
		// 오류가 나지 않을 경우 해당 댓글 좋아요 추가
		commentLikeRepository.save(new CommentLike(user, comment));
		comment.insertLikeCnt();
		commentRepository.save(comment);
	}

	// 선택한 댓글 좋아요 취소
	@Override
	@Transactional
	@CommentCheckPostId // postId 받은 것과 comment DB에 저장된 postId가 다를 경우 예외 처리
	public void commentDeleteLike(Long postId, Long commentId, User user) {
		Comment comment = findComment(commentId);
		// 좋아요를 누른 적이 없는 경우 오류 코드 반환
		if (findCommentLike(user, comment) == null) {
			throw new IllegalArgumentException("좋아요를 누르시지 않았습니다.");
		}
		commentLikeRepository.delete(findCommentLike(user, comment));
		comment.deleteLikeCnt();
		commentRepository.save(comment);
	}

	// id에 따른 댓글 찾기
	public Comment findComment(Long commentId) {
		return commentRepository.findById(commentId).orElseThrow(() ->
				new NotFoundException("선택한 댓글은 존재하지 않습니다.")
		);
	}

	// 사용자와 댓글에 따른 좋아요 찾기
    private CommentLike findCommentLike(User user, Comment comment) {
        return commentLikeRepository.findByUserAndComment(user,comment).orElse(null);
    }

	// id에 따른 게시글 찾기
	private Post findPost(Long postId) {
		return postRepository.findById(postId).orElseThrow(() ->
				new NotFoundException("선택한 게시글은 존재하지 않습니다.")
		);
	}
}
