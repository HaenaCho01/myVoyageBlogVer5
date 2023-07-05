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
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment", nullable = false, length = 100)
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username", nullable = false)
    private User user;

    @Column(name = "likeCnt")
    private long likeCnt;

    @OneToMany(mappedBy = "comment", orphanRemoval = true)
    private List<CommentLike> commentLikeList;

    public Comment(Post post, CommentRequestDto requestDto, User user) {
        this.post = post;
        this.comment = requestDto.getComment();
        this.user = user;
    }

    public void update(CommentRequestDto requestDto) {
        this.comment = requestDto.getComment();
    }

    public void insertLikeCnt() {
        this.likeCnt++;
    }

    public void deleteLikeCnt() {
        this.likeCnt--;
    }
}
