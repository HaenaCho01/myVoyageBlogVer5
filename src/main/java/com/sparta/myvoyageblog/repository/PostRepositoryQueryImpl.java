package com.sparta.myvoyageblog.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.myvoyageblog.entity.Post;
import com.sparta.myvoyageblog.entity.QPost;
import com.sparta.myvoyageblog.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostRepositoryQueryImpl implements PostRepositoryQuery {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Page<Post> search(PostSearchCond cond, Pageable pageable) {
		QPost post = QPost.post;

		// 제목 또는 내용 중에서 키워드를 포함하는 게시글을 검색 (OR 연산)
		BooleanExpression titleContainsKeyword = post.title.contains(cond.getKeyword());
		BooleanExpression contentContainsKeyword = post.content.contains(cond.getKeyword());

		var query = jpaQueryFactory.select(post)
				.from(post)
				.where(titleContainsKeyword.or(contentContainsKeyword))
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.orderBy(post.createdAt.desc()); // 작성일자 내림차순 정렬

		var posts = query.fetch();

		if (posts.isEmpty()) {
			throw new NotFoundException("해당 페이지는 존재하지 않습니다.");
		}

		long totalSize = jpaQueryFactory.select(Wildcard.count)
				.from(post)
				.where(
						titleContainsKeyword.or(contentContainsKeyword)
				).fetch().get(0);

		return PageableExecutionUtils.getPage(posts, pageable, () -> totalSize);
	}
}