package com.sparta.myvoyageblog.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class PostListResponseDto extends ApiResponseDto {
	private List<PostResponseDto> postList;

	public PostListResponseDto(List<PostResponseDto> postList) {
		this.postList = postList;
	}
}
