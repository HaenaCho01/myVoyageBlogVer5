package com.sparta.myvoyageblog.repository;

import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Post getById(Long id);

    List<Post> findAllByOrderByCreatedAtDesc();
}
