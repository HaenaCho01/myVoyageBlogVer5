package com.sparta.myvoyageblog.repository;

import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.PostLike;
import com.sparta.myvoyageblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository <PostLike, Long> {
    Optional<PostLike> findByUserAndPost(User user, Post post);
}
