package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.dto.PostResponseDto;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.repository.PostRepository;
import com.sparta.myvoyageblog.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto, user);
        Post savePost = postRepository.save(post);
        PostResponseDto postResponseDto = new PostResponseDto(savePost);
        return postResponseDto;
    }

    public List<PostResponseDto> getPosts(User user) {
        return postRepository.findAllByUsernameOrderByCreatedAtDesc(user.getUsername()).stream().map(PostResponseDto::new).toList();
    }

    @Transactional
    public PostResponseDto getPostById(Long id, User user) {
        Post post = checkUser(findPost(id), user.getUsername());
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        Post post = checkUser(findPost(id), user.getUsername());
        post.update(requestDto, user);
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    public void deletePost(Long id, @AuthenticationPrincipal User user) {
        Post post = checkUser(findPost(id), user.getUsername());
        postRepository.delete(post);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }

    private Post checkUser(Post selectPost, String username) {
        if (username.equals(selectPost.getUsername())) {
            return selectPost;
        } else {
            throw new IllegalArgumentException("해당 게시글에 대한 권한이 없습니다.");
        }
    }
}
