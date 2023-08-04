package com.sparta.myvoyageblog.dto;

import com.sparta.myvoyageblog.entity.PostMedia;
import lombok.Getter;

@Getter
public class PostMediaResponseDto extends ApiResponseDto {
	private Long id;
	private String mediaUrl;

	public PostMediaResponseDto(PostMedia postMedia) {
		this.id = postMedia.getId();
		this.mediaUrl = postMedia.getMediaUrl();
	}
}