package com.sparta.myvoyageblog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "postMedia")
@NoArgsConstructor
public class PostMedia extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mediaUrl", nullable = false)
    String mediaUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId", nullable = false)
    private Post post;

    public PostMedia(Post post, String mediaUrl) {
        this.post = post;
        this.mediaUrl = mediaUrl;
    }

    public void update(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }
}