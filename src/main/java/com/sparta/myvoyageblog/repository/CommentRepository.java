package com.sparta.myvoyageblog.repository;

import com.sparta.myvoyageblog.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
