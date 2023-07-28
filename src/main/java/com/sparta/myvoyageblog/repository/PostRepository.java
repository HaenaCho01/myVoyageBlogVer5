package com.sparta.myvoyageblog.repository;

import com.sparta.myvoyageblog.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

@RepositoryDefinition(domainClass = Post.class, idClass = Long.class)
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryQuery {
	List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
