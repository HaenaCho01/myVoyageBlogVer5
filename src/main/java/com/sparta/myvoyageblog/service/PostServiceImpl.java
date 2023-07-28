package com.sparta.myvoyageblog.service;

import com.sparta.myvoyageblog.dto.PageDto;
import com.sparta.myvoyageblog.dto.PostListResponseDto;
import com.sparta.myvoyageblog.dto.PostRequestDto;
import com.sparta.myvoyageblog.dto.PostResponseDto;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.PostLike;
import com.sparta.myvoyageblog.entity.User;
import com.sparta.myvoyageblog.exception.NotFoundException;
import com.sparta.myvoyageblog.exception.annotation.PostCheckUserNotEquals;
import com.sparta.myvoyageblog.repository.PostLikeRepository;
import com.sparta.myvoyageblog.repository.PostRepository;
import com.sparta.myvoyageblog.repository.PostRepositoryQueryImpl;
import com.sparta.myvoyageblog.repository.PostSearchCond;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CommentServiceImpl commentService;
    private final PostLikeRepository postLikeRepository;

    @Autowired
    private final PostRepositoryQueryImpl postRepositoryQueryImpl;

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
    public PostListResponseDto getPosts(PageDto pageDto) {
        List<PostResponseDto> postResponseDtoList = postRepository.findAllByOrderByCreatedAtDesc(pageDto.toPageable()).stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new PostListResponseDto(postResponseDtoList);
    }

    // 선택한 게시글 및 댓글 조회
    @Override
    @Transactional(readOnly = true)
    public PostResponseDto getPostById(Long postId) {
        return new PostResponseDto(findPost(postId));
    }

    // 키워드 검색으로 게시글 조회
    @Override
    @Transactional(readOnly = true)
    public PostListResponseDto searchPostsByKeyword(String keyword, PageDto pageDto) {
        // PostSearchCond 객체를 생성하고 키워드를 설정
        PostSearchCond searchCond = new PostSearchCond();
        searchCond.setKeyword(keyword);

        // PostRepositoryQuery 빈의 search 메서드를 호출하여 검색 결과를 받아옴
        Page<Post> searchedPosts = postRepositoryQueryImpl.search(searchCond, pageDto.toPageable());

        // 검색 결과를 PostResponseDto 리스트로 변환하여 반환
        List<PostResponseDto> postResponseDtoList = searchedPosts.stream()
                .map(PostResponseDto::new)
                .collect(Collectors.toList());

        return new PostListResponseDto(postResponseDtoList);
    }

    // 선택한 게시글 수정
    @Override
    @Transactional
    @PostCheckUserNotEquals // 다른 유저가 수정을 시도할 경우 예외 처리
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, User user) {
        findPost(postId).update(requestDto);
        PostResponseDto postResponseDto = new PostResponseDto(findPost(postId));
        return postResponseDto;
    }

    // 선택한 게시글 삭제
    @Override
    @Transactional
    @PostCheckUserNotEquals // 다른 유저가 삭제를 시도할 경우 예외 처리
    public void deletePost(Long postId, User user) {
        postRepository.delete(findPost(postId));
    }

    // 선택한 게시글 좋아요 기능 추가
    @Override
    @Transactional
    public void postInsertLike(Long postId, User user) {
        Post post = findPost(postId);
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
}
