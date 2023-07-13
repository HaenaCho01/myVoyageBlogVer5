package com.sparta.myvoyageblog.entity;

import com.sparta.myvoyageblog.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "posts")
@NoArgsConstructor
public class Post extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Column(name = "likeCnt")
    private long likeCnt;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<PostLike> postLikeList;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments;

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }

    public void insertLikeCnt() {
        this.likeCnt++;
    }

    public void deleteLikeCnt() {
        this.likeCnt--;
    }
}
