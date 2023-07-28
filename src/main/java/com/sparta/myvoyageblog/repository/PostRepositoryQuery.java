package com.sparta.myvoyageblog.repository;

import com.sparta.myvoyageblog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryQuery {
	Page<Post> search(PostSearchCond cond, Pageable pageable);
}
