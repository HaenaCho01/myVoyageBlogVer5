package com.sparta.myvoyageblog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "commentLike")
public class CommentLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "commentId")
	private Comment comment;

	public CommentLike(User user, Comment comment) {
		this.user = user;
		this.comment = comment;
	}
}
