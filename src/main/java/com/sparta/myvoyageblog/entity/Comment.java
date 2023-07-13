package com.sparta.myvoyageblog.entity;

import com.sparta.myvoyageblog.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends Timestamped {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(name = "likeCnt")
    private long likeCnt;

    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    private List<CommentLike> commentLikeList;

    public Comment(Post post, CommentRequestDto requestDto, User user) {
        this.post = post;
        this.content = requestDto.getContent();
        this.user = user;
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }

    public void insertLikeCnt() {
        this.likeCnt++;
    }

    public void deleteLikeCnt() {
        this.likeCnt--;
    }
}
