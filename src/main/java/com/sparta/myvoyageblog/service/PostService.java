package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.dto.PostResponseDto;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.repository.PostRepository;
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

    // 게시글 작성
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto, user);
        Post savePost = postRepository.save(post);
        PostResponseDto postResponseDto = new PostResponseDto(savePost);
        return postResponseDto;
    }

    // 전체 게시글 목록 조회
    public List<PostResponseDto> getPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    // 선택한 게시글 조회
    @Transactional
    public PostResponseDto getPostById(Long id) {
        PostResponseDto responseDto = new PostResponseDto(findPost(id));
        return responseDto;
    }

    // 선택한 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        checkUser(id, user).update(requestDto);
        PostResponseDto postResponseDto = new PostResponseDto(findPost(id));
        return postResponseDto;
    }

    // 선택한 게시글 삭제
    public void deletePost(Long id, @AuthenticationPrincipal User user) {
        postRepository.delete(checkUser(id, user));
    }

    // id에 따른 게시글 찾기
    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }

    // 선택한 게시글의 사용자가 맞는지 혹은 관리자인지 확인하기
    private Post checkUser(Long selectId, User user) {
        Post post = findPost(selectId);
        if (post.getUser().getUsername().equals(user.getUsername()) || user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            return post;
        } else {
            throw new IllegalArgumentException("해당 게시글에 관한 권한이 없습니다.");
        }
    }
}
