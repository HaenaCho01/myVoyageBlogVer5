package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.dto.PostResponseDto;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.PostLike;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.exception.NotFoundException;
import com.sparta.myvoyageblog.exception.UnauthorizedException;
import com.sparta.myvoyageblog.repository.PostLikeRepository;
import com.sparta.myvoyageblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentServiceImpl commentService;
    private final PostLikeRepository postLikeRepository;

    // 게시글 작성
    @Override
    @Transactional
    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto, user);
        Post savePost = postRepository.save(post);
        PostResponseDto postResponseDto = new PostResponseDto(savePost);
        return postResponseDto;
    }

    // 전체 게시글 및 댓글 목록 조회
    @Override
    @Transactional(readOnly = true)
    public List<List<Object>> getPosts() {
        List<Post> postList = postRepository.findAllByOrderByCreatedAtDesc();

        List<List<Object>> postAndCommentsList = new ArrayList<>();

        for (int i = 0; i < postList.size(); i++) {
            postAndCommentsList.add(getPostById(postList.get(i).getId()));
        }
        return postAndCommentsList;
    }

    // 선택한 게시글 및 댓글 조회
    @Override
    @Transactional(readOnly = true)
    public List<Object> getPostById(Long postId) {
        List<Object> postAndComments = new ArrayList<>();
        postAndComments.add(new PostResponseDto(findPost(postId)));
        postAndComments.add(commentService.getCommentsByPostId(postId));
        return postAndComments;
    }

    // 선택한 게시글 수정
    @Override
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, User user) {
        // 다른 유저가 수정을 시도할 경우 예외 처리
        if (!checkUser(postId, user)) {
            throw new UnauthorizedException("작성자만 수정할 수 있습니다.");
        }
        // 오류가 나지 않을 경우 해당 게시글 수정
        findPost(postId).update(requestDto);
        PostResponseDto postResponseDto = new PostResponseDto(findPost(postId));
        return postResponseDto;
    }

    // 선택한 게시글 삭제
    @Override
    @Transactional
    public void deletePost(Long postId, @AuthenticationPrincipal User user) {
        // 다른 유저가 삭제를 시도할 경우 예외 처리
        if (!checkUser(postId, user)) {
            throw new UnauthorizedException("작성자만 삭제할 수 있습니다.");
        }
        // 오류가 나지 않을 경우 해당 게시글 삭제
        postRepository.delete(findPost(postId));
    }

    // 선택한 게시글 좋아요 기능 추가
    @Override
    @Transactional
    public void postInsertLike(Long postId, User user) {
        Post post = findPost(postId);
        // 작성자가 좋아요를 시도할 경우 오류 코드 반환
        if (checkUser(postId, user)) {
            throw new UnauthorizedException("작성자는 좋아요를 누를 수 없습니다.");
        }
        // 좋아요를 이미 누른 경우 오류 코드 반환
        if (findPostLike(user, post) != null) {
            throw new IllegalArgumentException("좋아요를 이미 누르셨습니다.");
        }
        // 오류가 나지 않을 경우 해당 댓글 좋아요 추가
        postLikeRepository.save(new PostLike(user, post));
        post.insertLikeCnt();
        postRepository.save(post);
    }

    // 선택한 게시글 좋아요 취소
    @Override
    @Transactional
    public void postDeleteLike(Long postId, User user) {
        Post post = findPost(postId);
        // 작성자가 좋아요를 시도할 경우 오류 코드 반환
        if (checkUser(postId, user)) {
            throw new UnauthorizedException("작성자는 좋아요를 누를 수 없습니다.");
        }
        // 좋아요를 이미 누른 경우 오류 코드 반환
        if (findPostLike(user, post) == null) {
            throw new IllegalArgumentException("좋아요를 누르시지 않았습니다.");
        }
        // 오류가 나지 않을 경우 해당 댓글 좋아요 추가
        postLikeRepository.delete(findPostLike(user, post));
        post.deleteLikeCnt();
        postRepository.save(post);
    }

    // id에 따른 게시글 찾기
    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new NotFoundException("선택한 게시글은 존재하지 않습니다.")
        );
    }

    // 사용자와 댓글에 따른 좋아요 찾기
    private PostLike findPostLike(User user, Post post) {
        return postLikeRepository.findByUserAndPost(user,post).orElse(null);
    }

    // 선택한 게시글의 사용자가 맞는지 혹은 관리자인지 확인하기
    private boolean checkUser(Long selectId, User user) {
        Post post = findPost(selectId);
        return post.getUser().getUsername().equals(user.getUsername()) || user.getRole().getAuthority().equals("ROLE_ADMIN");
    }
}
