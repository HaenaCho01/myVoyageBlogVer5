package com.sparta.myvoyageblog.repository;

import com.sparta.myvoyageblog.entity.Post;

import java.util.List;

public interface PostRepositoryQuery {
	List<Post> search(PostSearchCond cond);
}
