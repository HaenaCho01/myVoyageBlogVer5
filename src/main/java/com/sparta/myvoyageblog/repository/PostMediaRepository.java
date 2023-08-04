package com.sparta.myvoyageblog.repository;

import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {
	List<PostMedia> findAllByPostOrderByCreatedAt(Post post);
}