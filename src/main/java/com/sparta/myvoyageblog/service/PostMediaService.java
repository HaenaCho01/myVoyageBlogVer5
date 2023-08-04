package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.PostMediaResponseDto;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.PostMedia;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.exception.NotFoundException;
import com.sparta.myvoyageblog.exception.annotation.MediaCheckPostId;
import com.sparta.myvoyageblog.exception.annotation.MediaCheckUserNotEquals;
import com.sparta.myvoyageblog.exception.annotation.PostCheckUserNotEquals;
import com.sparta.myvoyageblog.repository.PostMediaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostMediaService {

	private final S3UploadService s3UploadService;
	private final PostMediaRepository postMediaRepository;
	private final PostServiceImpl postService;

	@Transactional(readOnly = true)
	// 선택한 게시글의 미디어 전체 조회
	public List<PostMediaResponseDto> getMediaByPostId(Long postId) {
		return postMediaRepository.findAllByPostOrderByCreatedAt(postService.findPost(postId)).stream().map(PostMediaResponseDto::new).toList();
	}

	// 미디어 S3 업로드 및 URL DB 저장
	@Transactional
	@PostCheckUserNotEquals
	public PostMediaResponseDto uploadMedia(Long postId, MultipartFile multipartFile, User user) throws IOException {
		Post post = postService.findPost(postId);

		// 업로드 할 파일을 선택했는지 확인하기
		if (multipartFile.getSize() == 0) {
			throw new IllegalArgumentException("업로드할 미디어를 선택해주세요");
		}

		else {
			// AWS S3 저장
			String mediaUrl = s3UploadService.uploadFile(multipartFile);
			// DB 저장
			PostMedia postMedia = new PostMedia(post, mediaUrl);
			PostMedia savedPostMedia = postMediaRepository.save(postMedia);
			return new PostMediaResponseDto(savedPostMedia);
		}
	}

	// 미디어 S3 삭제 및 재업로드 및 URL DB 수정
	@Transactional
	@MediaCheckPostId
	@MediaCheckUserNotEquals
	public PostMediaResponseDto updateMedia(Long postId, Long mediaId, MultipartFile multipartFile, User user) throws IOException {
		PostMedia postMedia = findMedia(mediaId);

		// 업로드 할 파일을 선택했는지 확인하기
		if (multipartFile.getSize() == 0) {
			throw new IllegalArgumentException("수정할 미디어를 선택해주세요");
		}

		else {
			// AWS S3 삭제
			String deletedMediaUrl = postMedia.getMediaUrl();
			s3UploadService.deleteFile(deletedMediaUrl);
			// AWS S3 저장
			String updatedMediaUrl = s3UploadService.uploadFile(multipartFile);
			// DB 수정
			postMedia.update(updatedMediaUrl);
			PostMedia updatedPostMedia = postMediaRepository.save(postMedia);
			return new PostMediaResponseDto(updatedPostMedia);
		}
	}

	// 미디어 S3 삭제 및 URL DB 삭제
	@Transactional
	@MediaCheckPostId
	@MediaCheckUserNotEquals
	public void deleteMedia(Long postId, Long mediaId, User user) {
		PostMedia postMedia = findMedia(mediaId);

		// AWS S3 삭제
		String mediaUrl = postMedia.getMediaUrl();
		s3UploadService.deleteFile(mediaUrl);
		// DB 삭제
		postMediaRepository.delete(postMedia);
	}

	// mediaId로 postMedia 찾기
	public PostMedia findMedia(Long mediaId) {
		return postMediaRepository.findById(mediaId).orElseThrow(
				() -> new NotFoundException("해당 미디어를 찾을 수 없습니다.")
		);
	}
}