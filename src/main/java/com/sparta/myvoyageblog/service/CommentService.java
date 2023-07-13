package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.CommentRequestDto;
import com.sparta.myvoyageblog.dto.CommentResponseDto;
import com.sparta.myvoyageblog.entity.Comment;
import com.sparta.myvoyageblog.entity.CommentLike;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.repository.CommentLikeRepository;
import com.sparta.myvoyageblog.repository.CommentRepository;
import com.sparta.myvoyageblog.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
	private final CommentLikeRepository commentLikeRepository;

    // 선택한 게시글에 대한 댓글 전체 조회
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
	    return commentRepository.findAllByPostOrderByCreatedAtDesc(findPost(postId)).stream().map(CommentResponseDto::new).toList();
    }

	// 댓글 작성
	@Transactional
    public CommentResponseDto createComment(Long postid, CommentRequestDto requestDto, User user) {
		Post post = findPost(postid);
        Comment comment = new Comment(post, requestDto, user);
        Comment saveComment = commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(saveComment);
        return commentResponseDto;
    }

	// 선택한 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long postId, Long commentId, CommentRequestDto requestDto, User user) {
		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우 예외 처리
		if (postId != findComment(commentId).getPost().getId()) {
			throw new EntityNotFoundException("해당 페이지를 찾을 수 없습니다.");
		}
		// 다른 유저가 수정을 시도할 경우 예외 처리
		if (!checkUser(commentId, user)) {
			throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
		}
		// 오류가 나지 않을 경우 해당 댓글 수정
		findComment(commentId).update(requestDto);
		CommentResponseDto commentResponseDto = new CommentResponseDto(findComment(commentId));
		return commentResponseDto;
    }

	// 선택한 댓글 삭제
	@Transactional
	public void deleteComment(Long postId, Long commentId, @AuthenticationPrincipal User user) {
		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우 예외 처리
		if (postId != findComment(commentId).getPost().getId()) {
			throw new EntityNotFoundException("해당 페이지를 찾을 수 없습니다.");
		}
		// 다른 유저가 삭제를 시도할 경우 예외 처리
		if (!checkUser(commentId, user)) {
			throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
		}
		// 오류가 나지 않을 경우 해당 댓글 삭제
		commentRepository.delete(findComment(commentId));
	}

	// 선택한 댓글 좋아요 기능 추가
	@Transactional
	public void commentInsertLike(Long postId, Long commentId, User user) {
		Comment comment = findComment(commentId);
		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우 예외 처리
		if (postId != comment.getPost().getId()) {
			throw new EntityNotFoundException("해당 페이지를 찾을 수 없습니다.");
		}
		// 작성자가 좋아요를 시도할 경우 오류 코드 반환
		if (checkUser(commentId, user)) {
			throw new IllegalArgumentException("작성자는 좋아요를 누를 수 없습니다.");
		}
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
	@Transactional
	public void commentDeleteLike(Long postId, Long commentId, User user) {
		Comment comment = findComment(commentId);
		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우 예외 처리
		if (postId != comment.getPost().getId()) {
			throw new EntityNotFoundException("해당 페이지를 찾을 수 없습니다.");
		}
		// 작성자가 좋아요를 시도할 경우 오류 코드 반환
		if (checkUser(commentId, user)) {
			throw new IllegalArgumentException("작성자는 좋아요를 누를 수 없습니다.");
		}
		// 좋아요를 누른 적이 없는 경우 오류 코드 반환
		if (findCommentLike(user, comment) == null) {
			throw new IllegalArgumentException("좋아요를 누르시지 않았습니다.");
		}
		commentLikeRepository.delete(findCommentLike(user, comment));
		comment.deleteLikeCnt();
		commentRepository.save(comment);
	}

	// id에 따른 댓글 찾기
	private Comment findComment(Long commentid) {
		return commentRepository.findById(commentid).orElseThrow(() ->
				new EntityNotFoundException("선택한 댓글은 존재하지 않습니다.")
		);
	}

	// 사용자와 댓글에 따른 좋아요 찾기
    private CommentLike findCommentLike(User user, Comment comment) {
        return commentLikeRepository.findByUserAndComment(user,comment).orElse(null);
    }

	// id에 따른 게시글 찾기
	private Post findPost(Long id) {
		return postRepository.findById(id).orElseThrow(() ->
				new EntityNotFoundException("선택한 게시글은 존재하지 않습니다.")
		);
	}

	// 선택한 댓글의 사용자가 맞는지 혹은 관리자인지 확인하기
    private boolean checkUser(Long selectId, User user) {
        Comment comment = findComment(selectId);
        if (comment.getUser().getUsername().equals(user.getUsername()) || user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            return true;
        } else {
            return false;
        }
    }
}
