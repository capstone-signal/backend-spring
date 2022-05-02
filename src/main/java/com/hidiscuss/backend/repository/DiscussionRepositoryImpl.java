package com.hidiscuss.backend.repository;

import com.hidiscuss.backend.controller.dto.GetDiscussionsDto;
import com.hidiscuss.backend.controller.dto.UserResponseDto;
import com.hidiscuss.backend.entity.*;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DiscussionRepositoryImpl implements DiscussionRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final QDiscussion qDiscussion = QDiscussion.discussion;

    public DiscussionRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Discussion findByIdFetchOrNull(Long id) {
        return queryFactory.selectFrom(qDiscussion)
                .where(qDiscussion.id.eq(id))
                .fetchOne();
    }

    @Override
    public Page<Discussion> findAllFilteredFetch(GetDiscussionsDto dto, PageRequest pageRequest) {
        JPAQuery<Discussion> query = queryFactory.selectFrom(qDiscussion)
                .where(qDiscussion.state.eq(dto.getState())
                        .and(qDiscussion.title.contains(dto.getKeyword())));
                //tag

        Optional<Long> userId = Optional.ofNullable(dto.getUserId());
        if (userId.isPresent())
            query.where(qDiscussion.user.id.eq(userId.get()));

        query.offset(pageRequest.getOffset()).limit(pageRequest.getPageSize());

        for (Sort.Order o : pageRequest.getSort()) {
            PathBuilder pathBuilder = new PathBuilder(qDiscussion.getType(), qDiscussion.getMetadata());
            OrderSpecifier orderSpecifier = new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(o.getProperty()));
            query.orderBy(orderSpecifier);
        }
        List<Discussion> result = query.fetch();
        return new PageImpl<>(result, pageRequest, result.size());
    }
}
