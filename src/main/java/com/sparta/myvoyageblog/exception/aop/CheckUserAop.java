package com.sparta.myvoyageblog.exception.aop;

import com.sparta.myvoyageblog.dto.CommentRequestDto;
import com.sparta.myvoyageblog.entity.Comment;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.PostMedia;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.exception.UnauthorizedException;
import com.sparta.myvoyageblog.service.CommentServiceImpl;
import com.sparta.myvoyageblog.service.PostMediaService;
import com.sparta.myvoyageblog.service.PostServiceImpl;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
public class CheckUserAop {

	private final PostServiceImpl postServiceImpl;
	private final CommentServiceImpl commentServiceImpl;
	private final PostMediaService postMediaService;

	@Autowired
	public CheckUserAop(PostServiceImpl postServiceImpl, CommentServiceImpl commentServiceImpl, PostMediaService postMediaService) {
		this.postServiceImpl = postServiceImpl;
		this.commentServiceImpl = commentServiceImpl;
		this.postMediaService = postMediaService;
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
	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.PostCheckUserNotEquals) && args(postId, object, user)")
	public void postCheckUserNotEqualsUpdate(JoinPoint joinPoint, Long postId, Object object, User user) {
		boolean isAdmin = user.getRole().getAuthority().equals("ROLE_ADMIN");
		// Post Entity 가져오기
		Post post = postServiceImpl.findPost(postId);
		if (!post.getUser().getUsername().equals(user.getUsername()) && !isAdmin) {
			throw new UnauthorizedException("작성자/관리자만 접근할 수 있습니다.");
		}
	}

	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.CommentCheckUserNotEquals) && args(postId, commentId, user)")
	public void commentCheckUserNotEquals(JoinPoint joinPoint, Long postId, Long commentId, User user) {
		boolean isAdmin = user.getRole().getAuthority().equals("ROLE_ADMIN");
		// Comment Entity 가져오기
		Comment comment = commentServiceImpl.findComment(commentId);
		if (!comment.getUser().getUsername().equals(user.getUsername()) && !isAdmin) {
			throw new UnauthorizedException("작성자/관리자만 접근할 수 있습니다.");
		}
	}

	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.CommentCheckUserNotEquals) && args(postId, commentId, requestDto, user)")
	public void commentCheckUserNotEqualsUpdate(JoinPoint joinPoint, Long postId, Long commentId, CommentRequestDto requestDto, User user) {
		boolean isAdmin = user.getRole().getAuthority().equals("ROLE_ADMIN");
		// Comment Entity 가져오기
		Comment comment = commentServiceImpl.findComment(commentId);
		if (!comment.getUser().getUsername().equals(user.getUsername()) && !isAdmin) {
			throw new UnauthorizedException("작성자/관리자만 접근할 수 있습니다.");
		}
	}

	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.MediaCheckUserNotEquals) && args(postId, mediaId, multipartFile, user)")
	public void mediaCheckUserNotEqualsUpdate(JoinPoint joinPoint, Long postId, Long mediaId, MultipartFile multipartFile, User user) {
		boolean isAdmin = user.getRole().getAuthority().equals("ROLE_ADMIN");
		// Media Entity 가져오기
		PostMedia media = postMediaService.findMedia(mediaId);
		if (!media.getPost().getUser().getUsername().equals(user.getUsername()) && !isAdmin) {
			throw new UnauthorizedException("작성자/관리자만 접근할 수 있습니다.");
		}
	}

	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.MediaCheckUserNotEquals) && args(postId, mediaId, user)")
	public void mediaCheckUserNotEquals(JoinPoint joinPoint, Long postId, Long mediaId, User user) {
		boolean isAdmin = user.getRole().getAuthority().equals("ROLE_ADMIN");
		// Media Entity 가져오기
		PostMedia postMedia = postMediaService.findMedia(mediaId);
		if (!postMedia.getPost().getUser().getUsername().equals(user.getUsername()) && !isAdmin) {
			throw new UnauthorizedException("작성자/관리자만 접근할 수 있습니다.");
		}
	}
}