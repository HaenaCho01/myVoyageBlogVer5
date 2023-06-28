package com.sparta.myvoyageblog.repository;

import com.sparta.myvoyageblog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findTop1ByOrderByCreatedAtDesc();
    Post findTop1ByOrderByCreatedAtAsc();
}
