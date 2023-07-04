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
import jakarta.servlet.http.HttpServletResponse;
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
    public List<CommentResponseDto> getCommentsByPostId(Long postid) {
	    return commentRepository.findAllByPost_idOrderByCreatedAtDesc(postid).stream().map(CommentResponseDto::new).toList();
    }

	// 댓글 작성
    public CommentResponseDto createComment(Long postid, CommentRequestDto requestDto, User user) {
		Post post = postRepository.getById(postid);
        Comment comment = new Comment(post, requestDto, user);
        Comment saveComment = commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(saveComment);
        return commentResponseDto;
    }

	// 선택한 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long postid, Long commentid, CommentRequestDto requestDto, User user, HttpServletResponse response) {
	    if (postid != findComment(commentid).getPost().getId()) {
		    response.setStatus(404);
		    return null;
	    } else if (!checkUser(commentid, user)) {
		    response.setStatus(400);
			return null;
	    } else {
			findComment(commentid).update(requestDto);
		    CommentResponseDto commentResponseDto = new CommentResponseDto(findComment(commentid));
		    return commentResponseDto;
	    }
    }

	// 선택한 댓글 삭제
    public void deleteComment(Long postid, Long commentid, @AuthenticationPrincipal User user, HttpServletResponse response) {
	    if (postid != findComment(commentid).getPost().getId()) {
		    response.setStatus(404);
	    } else if (!checkUser(commentid, user)) {
	        response.setStatus(400);
		} else {
		    commentRepository.delete(findComment(commentid));
	    }
    }

	@Transactional
	// 선택한 댓글 좋아요 기능 추가
	public CommentResponseDto commentInsertLike(Long postId, Long commentId, User user, HttpServletResponse response) {
		Comment comment = findComment(commentId);

		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우 오류 코드 반환
		if (postId != comment.getPost().getId()) {
			response.setStatus(404);
			return null;

		// 작성자가 좋아요를 시도할 경우 오류 코드 반환
		} else if (checkUser(commentId, user)) {
			response.setStatus(400);
			return null;

		// 좋아요를 이미 누른 경우 오류 코드 반환
		} else if (findCommentLike(user, comment) != null) {
			response.setStatus(409);
			return null;

		// 오류가 나지 않을 경우 해당 댓글 좋아요 추가
		} else {
			commentLikeRepository.save(new CommentLike(user, comment));
			comment.updateLikeCnt(commentLikeRepository.count());
			CommentResponseDto commentResponseDto = new CommentResponseDto(commentRepository.save(comment));
			return commentResponseDto;
		}
	}

	public CommentResponseDto commentDeleteLike(Long postId, Long commentId, User user, HttpServletResponse response) {
		Comment comment = findComment(commentId);

		// postId 받은 것과 comment DB에 저장된 postId가 다를 경우 오류 코드 반환
		if (postId != comment.getPost().getId()) {
			response.setStatus(404);
			return null;

		// 작성자가 좋아요를 시도할 경우 오류 코드 반환
		} else if (checkUser(commentId, user)) {
			response.setStatus(400);
			return null;

		// 좋아요를 누른 적이 없는 경우 오류 코드 반환
		} else if (findCommentLike(user, comment) == null) {
			response.setStatus(409);
			return null;

		// 오류가 나지 않을 경우 해당 댓글 좋아요 추가
		} else {
			commentLikeRepository.delete(findCommentLike(user, comment));
			comment.updateLikeCnt(commentLikeRepository.count());
			CommentResponseDto commentResponseDto = new CommentResponseDto(commentRepository.save(comment));
			return commentResponseDto;
		}
	}

	// id에 따른 댓글 찾기
	private Comment findComment(Long commentid) {
		return commentRepository.findById(commentid).orElseThrow(() ->
				new IllegalArgumentException("선택한 좋아요는 존재하지 않습니다.")
		);
	}

	// 사용자와 댓글에 따른 좋아요 찾기
    private CommentLike findCommentLike(User user, Comment comment) {
        return commentLikeRepository.findByUserAndComment(user,comment).orElse(null);
    }

	// id에 따른 게시글 찾기
	private Post findPost(Long id) {
		return postRepository.findById(id).orElseThrow(() ->
				new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
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
