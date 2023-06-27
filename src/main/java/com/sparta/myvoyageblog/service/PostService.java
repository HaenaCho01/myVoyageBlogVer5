package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.dto.PostResponseDto;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.repository.PostRepository;
import com.sparta.myvoyageblog.security.UserDetailsImpl;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
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
        return postRepository.findAllByUserOrderByCreatedAtDesc(user).stream().map(PostResponseDto::new).toList();
    }

    @Transactional
    public PostResponseDto getPostById(Long id, User user) {
        PostResponseDto responseDto = new PostResponseDto(checkUser(id, user));
        return responseDto;
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        checkUser(id, user).update(requestDto, user);
        PostResponseDto postResponseDto = new PostResponseDto(findPost(id));
        return postResponseDto;
    }

    public void deletePost(Long id, @AuthenticationPrincipal User user) {
        postRepository.delete(checkUser(id, user));
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }

    private Post checkUser(Long selectId, User user) {
        Post post = findPost(selectId);
        if (post.getUser().getUsername().equals(user.getUsername())) {
            return post;
        } else {
            throw new IllegalArgumentException("해당 게시글에 관한 권한이 없습니다.");
        }
    }
}
