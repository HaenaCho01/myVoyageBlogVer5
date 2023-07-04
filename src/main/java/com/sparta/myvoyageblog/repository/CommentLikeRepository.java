package com.sparta.myvoyageblog.repository;

import com.sparta.myvoyageblog.entity.Comment;
import com.sparta.myvoyageblog.entity.CommentLike;
import com.sparta.myvoyageblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
	Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
