package com.sparta.myvoyageblog.exception.aop;

import com.sparta.myvoyageblog.dto.CommentRequestDto;
import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.entity.Comment;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.exception.UnauthorizedException;
import com.sparta.myvoyageblog.service.CommentServiceImpl;
import com.sparta.myvoyageblog.service.PostServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckUserAop {

	private final PostServiceImpl postServiceImpl;
	private final CommentServiceImpl commentServiceImpl;

	@Autowired
	public CheckUserAop(PostServiceImpl postServiceImpl, CommentServiceImpl commentServiceImpl) {
		this.postServiceImpl = postServiceImpl;
		this.commentServiceImpl = commentServiceImpl;
	}

	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.PostCheckUserNotEquals) && args(postId, user)")
	public void postCheckUserNotEquals(JoinPoint joinPoint, Long postId, User user) {
		boolean isAdmin = user.getRole().getAuthority().equals("ROLE_ADMIN");
		// Post Entity 가져오기
		Post post = postServiceImpl.findPost(postId);
		if (!post.getUser().getUsername().equals(user.getUsername()) && !isAdmin) {
			throw new UnauthorizedException("작성자/관리자만 접근할 수 있습니다.");
		}
	}
	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.PostCheckUserNotEquals) && args(postId, requestDto, user)")
	public void postCheckUserNotEqualsUpdate(JoinPoint joinPoint, Long postId, PostRequestDto requestDto, User user) {
		boolean isAdmin = user.getRole().getAuthority().equals("ROLE_ADMIN");
		// Post Entity 가져오기
		Post post = postServiceImpl.findPost(postId);
		if (!post.getUser().getUsername().equals(user.getUsername()) && !isAdmin) {
			throw new UnauthorizedException("작성자/관리자만 접근할 수 있습니다.");
		}
	}

	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.CommentCheckUserNotEquals) && args(postId, commentId, user)")
	public void CommentCheckUserNotEquals(JoinPoint joinPoint, Long postId, Long commentId, User user) {
		boolean isAdmin = user.getRole().getAuthority().equals("ROLE_ADMIN");
		// Comment Entity 가져오기
		Comment comment = commentServiceImpl.findComment(commentId);
		if (!comment.getUser().getUsername().equals(user.getUsername()) && !isAdmin) {
			throw new UnauthorizedException("작성자/관리자만 접근할 수 있습니다.");
		}
	}
	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.CommentCheckUserNotEquals) && args(postId, commentId, requestDto, user)")
	public void CommentCheckUserNotEqualsUpdate(JoinPoint joinPoint, Long postId, Long commentId, CommentRequestDto requestDto, User user) {
		boolean isAdmin = user.getRole().getAuthority().equals("ROLE_ADMIN");
		// Comment Entity 가져오기
		Comment comment = commentServiceImpl.findComment(commentId);
		if (!comment.getUser().getUsername().equals(user.getUsername()) && !isAdmin) {
			throw new UnauthorizedException("작성자/관리자만 접근할 수 있습니다.");
		}
	}

}