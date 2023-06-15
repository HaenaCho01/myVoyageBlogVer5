package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.dto.PostResponseDto;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostResponseDto createPost(PostRequestDto requestDto) {
        Post post = new Post(requestDto);

        Post savePost = postRepository.save(post);

        PostResponseDto postResponseDto = new PostResponseDto(post);

        return postResponseDto;
    }

    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    @Transactional
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.getById(id);
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto) {
        Post post = checkPassword(findPost(id), requestDto.getPassword());

        post.update(requestDto);

        PostResponseDto postResponseDto = new PostResponseDto(post);

        return postResponseDto;
    }

    public void deletePost(Long id, String requestPassword) {
        Post post = checkPassword(findPost(id), requestPassword);

        postRepository.delete(post);
    }

    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }

    private Post checkPassword(Post selectPost, String requestPassword) {
        if (requestPassword.equals(selectPost.getPassword())) {
            return selectPost;
        } else {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
