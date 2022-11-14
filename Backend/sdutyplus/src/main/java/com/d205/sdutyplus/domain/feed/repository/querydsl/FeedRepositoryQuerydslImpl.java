package com.d205.sdutyplus.domain.feed.repository.querydsl;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.dto.QFeedResponseDto;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.global.entity.Job;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.d205.sdutyplus.domain.feed.entity.QFeed.feed;
import static com.d205.sdutyplus.domain.feed.entity.QFeedLike.feedLike;
import static com.d205.sdutyplus.domain.feed.entity.QScrap.scrap;
import static com.d205.sdutyplus.domain.off.entity.QOffFeed.offFeed;
import static com.d205.sdutyplus.domain.user.entity.QUser.user;
import static com.d205.sdutyplus.domain.warn.entity.QWarnFeed.warnFeed;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryQuerydslImpl implements FeedRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FeedResponseDto> findAllFeeds(Long userSeq) {
        return queryFactory.select(new QFeedResponseDto(
                                feed.seq,
                                feed.writerSeq,
                                feed.imgUrl,
                                feed.content
                        )
                ).from(feed)
                .where(
                        feed.banYN.eq(false)
                                .and(
                                        feed.notIn(
                                                JPAExpressions.select(offFeed.feed)
                                                        .where(offFeed.user.seq.eq(userSeq)).from(offFeed)
                                        )
                                )
                                .and(
                                        feed.notIn(
                                                JPAExpressions.select(warnFeed.feed)
                                                        .where(warnFeed.user.seq.eq(userSeq)).from(warnFeed)
                                        )
                                )
                )
                .fetch();
    }

    @Override
    public Page<FeedResponseDto> findMyFeedPage(Long writerSeq, Pageable pageable) {
        QueryResults<FeedResponseDto> result = queryFactory.select(new QFeedResponseDto(
                                feed.seq,
                                feed.writerSeq,
                                feed.imgUrl,
                                feed.content
                        )
                ).from(feed)
                .where(feed.writerSeq.eq(writerSeq))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<FeedResponseDto> findScrapFeedPage(User userObject, Pageable pageable) {
        QueryResults<FeedResponseDto> result = queryFactory
                .select(
                        new QFeedResponseDto(
                                scrap.feed.seq,
                                scrap.feed.writerSeq,
                                scrap.feed.imgUrl,
                                scrap.feed.content
                        )
                )
                .from(scrap)
                .where(scrap.user.eq(userObject))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<FeedResponseDto> findFilterFeedPage(Long userSeq, Job jobObject, Pageable pageable) {
        QueryResults<FeedResponseDto> result = queryFactory
                .select(
                        new QFeedResponseDto(
                                feed.seq,
                                feed.writerSeq,
                                feed.imgUrl,
                                feed.content
                        )
                )
                .from(feed)
                .where(feed.writerSeq.in(
                        JPAExpressions
                                .select(user.seq)
                                .from(user)
                                .where(user.job.eq(jobObject))
                )
                        .and(
                                feed.notIn(
                                        JPAExpressions.select(offFeed.feed)
                                                .where(offFeed.user.seq.eq(userSeq)).from(offFeed)
                                )
                        )
                        .and(
                                feed.notIn(
                                        JPAExpressions.select(warnFeed.feed)
                                                .where(warnFeed.user.seq.eq(userSeq)).from(warnFeed)
                                )
                        )
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public void deleteMyLikedFeed(Long userSeq) {
        queryFactory.delete(feedLike)
                .where(feedLike.feed.writerSeq.eq(userSeq));
    }

    @Override
    public void deleteMyScrapedFeed(Long userSeq) {
        queryFactory.delete(scrap)
                .where(scrap.feed.writerSeq.eq(userSeq));
    }


}
