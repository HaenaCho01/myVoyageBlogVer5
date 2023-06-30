package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.dto.PostResponseDto;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.repository.PostRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CommentService commentService;

    // 게시글 작성
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto, user);
        Post savePost = postRepository.save(post);
        PostResponseDto postResponseDto = new PostResponseDto(savePost);
        return postResponseDto;
    }

    // 전체 게시글 및 댓글 목록 조회
    public List<List<Object>> getPosts() {
        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();

        List<List<Object>> postAndCommentsList = new ArrayList<>();

        for (int i = 0; i < postList.size(); i++) {
            postAndCommentsList.add(getPostById(postList.get(i).getId()));
        }
        return postAndCommentsList;
    }

    // 선택한 게시글 및 댓글 조회
    @Transactional
    public List<Object> getPostById(Long id) {
        List<Object> postAndComments = new ArrayList<>();
        postAndComments.add(new PostResponseDto(findPost(id)));
        postAndComments.add(commentService.getCommentsByPostId(id));
        return postAndComments;
    }

    // 선택한 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user, HttpServletResponse response) {
        if (!checkUser(id, user)) {
            response.setStatus(400);
            return null;
        } else {
            findPost(id).update(requestDto);
            PostResponseDto postResponseDto = new PostResponseDto(findPost(id));
            return postResponseDto;
        }
    }

    // 선택한 게시글 삭제
    public void deletePost(Long id, @AuthenticationPrincipal User user, HttpServletResponse response) {
        if (!checkUser(id, user)) {
            response.setStatus(400);
        } else {
            postRepository.delete(findPost(id));
        }
    }

    // id에 따른 게시글 찾기
    private Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("선택한 게시글은 존재하지 않습니다.")
        );
    }

    // 선택한 게시글의 사용자가 맞는지 혹은 관리자인지 확인하기
    private boolean checkUser(Long selectId, User user) {
        Post post = findPost(selectId);
        if (post.getUser().getUsername().equals(user.getUsername()) || user.getRole().getAuthority().equals("ROLE_ADMIN")) {
            return true;
        } else {
            return false;
        }
    }
}
