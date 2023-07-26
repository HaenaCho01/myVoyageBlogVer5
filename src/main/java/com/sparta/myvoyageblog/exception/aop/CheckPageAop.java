package com.sparta.myvoyageblog.exception.aop;

import com.sparta.myvoyageblog.dto.CommentRequestDto;
import com.sparta.myvoyageblog.entity.Comment;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.exception.NotFoundException;
import com.sparta.myvoyageblog.service.CommentServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckPageAop {
	private final CommentServiceImpl commentServiceImpl;

	@Autowired
	public CheckPageAop(CommentServiceImpl commentServiceImpl) {
		this.commentServiceImpl = commentServiceImpl;
	}

	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.CommentCheckPostId) && args(postId, commentId, user)")
	public void commentCheckPostId(JoinPoint joinPoint, Long postId, Long commentId, User user) {
		// Comment Entity 가져오기
		Comment comment = commentServiceImpl.findComment(commentId);
		if (postId != comment.getPost().getId()) {
			throw new NotFoundException("해당 페이지를 찾을 수 없습니다.");
		}
	}

	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.CommentCheckPostId) && args(postId, commentId, requestDto, user)")
	public void commentCheckPostIdUpdate(JoinPoint joinPoint, Long postId, Long commentId, CommentRequestDto requestDto, User user) {
		// Comment Entity 가져오기
		Comment comment = commentServiceImpl.findComment(commentId);
		if (postId != comment.getPost().getId()) {
			throw new NotFoundException("해당 페이지를 찾을 수 없습니다.");
		}
	}

}