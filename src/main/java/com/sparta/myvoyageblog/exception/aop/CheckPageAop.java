package com.sparta.myvoyageblog.exception.aop;

import com.sparta.myvoyageblog.dto.CommentRequestDto;
import com.sparta.myvoyageblog.entity.Comment;
import com.sparta.myvoyageblog.entity.PostMedia;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.exception.NotFoundException;
import com.sparta.myvoyageblog.service.CommentServiceImpl;
import com.sparta.myvoyageblog.service.PostMediaService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Aspect
@Component
public class CheckPageAop {
	private final CommentServiceImpl commentServiceImpl;
	private final PostMediaService postMediaService;

	@Autowired
	public CheckPageAop(CommentServiceImpl commentServiceImpl, PostMediaService postMediaService) {
		this.commentServiceImpl = commentServiceImpl;
		this.postMediaService = postMediaService;
	}

	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.CommentCheckPostId) && args(postId, commentId, user)")
	public void commentCheckPostId(JoinPoint joinPoint, Long postId, Long commentId, User user) {
		// Comment Entity 가져오기
		Comment comment = commentServiceImpl.findComment(commentId);
		if (!postId.equals(comment.getPost().getId())) {
			throw new NotFoundException("해당 페이지를 찾을 수 없습니다.");
		}
	}

	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.CommentCheckPostId) && args(postId, commentId, requestDto, user)")
	public void commentCheckPostIdUpdate(JoinPoint joinPoint, Long postId, Long commentId, CommentRequestDto requestDto, User user) {
		// Comment Entity 가져오기
		Comment comment = commentServiceImpl.findComment(commentId);
		if (!postId.equals(comment.getPost().getId())) {
			throw new NotFoundException("해당 페이지를 찾을 수 없습니다.");
		}
	}

	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.MediaCheckPostId) && args(postId, mediaId, multipartFile, user)")
	public void mediaCheckPostIdUpdate(JoinPoint joinPoint, Long postId, Long mediaId, MultipartFile multipartFile, User user) {
		// Media Entity 가져오기
		PostMedia media = postMediaService.findMedia(mediaId);
		if (!postId.equals(media.getPost().getId())) {
			throw new NotFoundException("해당 페이지를 찾을 수 없습니다.");
		}
	}

	@Before("@annotation(com.sparta.myvoyageblog.exception.annotation.MediaCheckPostId) && args(postId, mediaId, user)")
	public void mediaCheckPostId(JoinPoint joinPoint, Long postId, Long mediaId, User user) {
		// Media Entity 가져오기
		PostMedia media = postMediaService.findMedia(mediaId);
		if (!postId.equals(media.getPost().getId())) {
			throw new NotFoundException("해당 페이지를 찾을 수 없습니다.");
		}
	}
}