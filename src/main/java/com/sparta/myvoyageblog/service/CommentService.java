package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.CommentRequestDto;
import com.sparta.myvoyageblog.dto.CommentResponseDto;
import com.sparta.myvoyageblog.entity.Comment;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.User;
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

    // 댓글 작성
    public CommentResponseDto createComment(CommentRequestDto requestDto, User user) {
        Post post = postRepository.getById(requestDto.getPostId());
        Comment comment = new Comment(post, requestDto, user);
        Comment saveComment = commentRepository.save(comment);
        CommentResponseDto commentResponseDto = new CommentResponseDto(saveComment);
        return commentResponseDto;
    }

    // 선택한 댓글 수정
    @Transactional
    public CommentResponseDto updateComment(Long id, CommentRequestDto requestDto, User user, HttpServletResponse response) {
	    if (!checkUser(id, user)) {
		    response.setStatus(400);
			return null;
	    } else {
		    findComment(id).update(requestDto);
		    CommentResponseDto commentResponseDto = new CommentResponseDto(findComment(id));
		    return commentResponseDto;
	    }
    }

    // 선택한 댓글 삭제
    public void deleteComment(Long id, @AuthenticationPrincipal User user, HttpServletResponse response) {
        if (!checkUser(id, user)) {
	        response.setStatus(400);
		} else {
			commentRepository.delete(findComment(id));
        }
    }

	// 선택한 댓글 좋아요 기능 추가
	public CommentResponseDto commentLike(Long id, User user, HttpServletResponse response) {
		if (checkUser(id, user)) {
			response.setStatus(400);
			return null;
		} else {
			Comment comment = findComment(id);
			comment.updateLikes();
			Comment savedComment = commentRepository.save(comment);
			CommentResponseDto commentResponseDto = new CommentResponseDto(savedComment);
			return commentResponseDto;
		}
	}

	// 선택한 게시글에 대한 댓글 조회
	public List<CommentResponseDto> getComments(Long postid) {
		return commentRepository.findAllByPost_idOrderByCreatedAtDesc(postid).stream().map(CommentResponseDto::new).toList();
	}

	// id에 따른 댓글 찾기
    private Comment findComment(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
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
