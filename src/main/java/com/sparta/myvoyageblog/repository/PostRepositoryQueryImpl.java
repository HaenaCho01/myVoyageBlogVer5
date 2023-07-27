package com.sparta.myvoyageblog.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostRepositoryQueryImpl implements PostRepositoryQuery {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<Post> search(PostSearchCond cond) {
		QPost post = QPost.post;

		// 제목 또는 내용 중에서 키워드를 포함하는 게시글을 검색 (OR 연산)
		BooleanExpression titleContainsKeyword = post.title.contains(cond.getKeyword());
		BooleanExpression contentContainsKeyword = post.content.contains(cond.getKeyword());
		var query = jpaQueryFactory.select(post)
				.from(post)
				.where(titleContainsKeyword.or(contentContainsKeyword))
				.orderBy(post.createdAt.desc()); // 작성일자 내림차순 정렬

		var posts = query.fetch();

		return posts;
	}
}