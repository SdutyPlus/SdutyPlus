package com.d205.sdutyplus.domain.feed.repository.querydsl;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.dto.QFeedResponseDto;
import com.d205.sdutyplus.domain.user.dto.QUserWriterProfileDto;
import com.d205.sdutyplus.domain.user.entity.QUser;
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

import java.util.Optional;

import static com.d205.sdutyplus.domain.feed.entity.QFeed.feed;
import static com.d205.sdutyplus.domain.feed.entity.QFeedLike.feedLike;
import static com.d205.sdutyplus.domain.feed.entity.QScrap.scrap;
import static com.d205.sdutyplus.domain.off.entity.QOffFeed.offFeed;
import static com.d205.sdutyplus.domain.user.entity.QUser.user;
import static com.d205.sdutyplus.domain.warn.entity.QWarnFeed.warnFeed;

@RequiredArgsConstructor
public class FeedRepositoryQuerydslImpl implements FeedRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FeedResponseDto> findAllFeeds(Long userSeq, Pageable pageable) {
        QueryResults<FeedResponseDto> result = queryFactory.select(new QFeedResponseDto(
                                feed.seq,
                                feed.writer,
//                                new QUserWriterProfileDto(
//                                        feed.writer.seq,
//                                        feed.writer.email,
//                                        feed.writer.nickname,
//                                        feed.writer.job.jobName,
//                                        feed.writer.imgUrl
//                                ),
                                feed.imgUrl,
                                feed.content,
                                feed.feedLikes.size(),
                                feed.scraps.size()
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
                .orderBy(feed.regTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    public Optional<FeedResponseDto> findFeedBySeq(Long feedSeq){
        return Optional.ofNullable(queryFactory.select(new QFeedResponseDto(
                                feed.seq,
                                feed.writer,
                                feed.imgUrl,
                                feed.content,
                                feed.feedLikes.size(),
                                feed.scraps.size()
                        )
                ).from(feed)
                .where(feed.seq.eq(feedSeq))
                .fetchOne());
    }


    @Override
    public Page<FeedResponseDto> findMyFeedPage(Long writerSeq, Pageable pageable) {
        QueryResults<FeedResponseDto> result = queryFactory.select(new QFeedResponseDto(
                                feed.seq,
                                feed.writer,
                                feed.imgUrl,
                                feed.content,
                                feed.feedLikes.size(),
                                feed.scraps.size()
                        )
                ).from(feed)
                .where(feed.writer.seq.eq(writerSeq))
                .orderBy(feed.regTime.desc())
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
                                scrap.feed.writer,
                                scrap.feed.imgUrl,
                                scrap.feed.content,
                                scrap.feed.feedLikes.size(),
                                scrap.feed.scraps.size()
                        )
                )
                .from(scrap)
                .where(scrap.user.eq(userObject))
                .orderBy(scrap.feed.regTime.desc())
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
                                feed.writer,
                                feed.imgUrl,
                                feed.content,
                                feed.feedLikes.size(),
                                feed.scraps.size()
                        )
                )
                .from(feed)
                .where(feed.writer.seq.in(
                        JPAExpressions
                                .select(user.seq)
                                .from(user)
                                .where(user.job.eq(jobObject))
                ))
                .orderBy(feed.regTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<FeedResponseDto> findAllWarnFeedPage(Pageable pageable) {
        QueryResults<FeedResponseDto> result = queryFactory
                .select(
                        new QFeedResponseDto(
                                warnFeed.feed.seq,
                                warnFeed.feed.writer,
                                warnFeed.feed.imgUrl,
                                warnFeed.feed.content,
                                warnFeed.feed.feedLikes.size(),
                                warnFeed.feed.scraps.size()
                        )
                )
                .from(warnFeed)
                .orderBy(warnFeed.feed.regTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }


    @Override
    public void deleteMyLikedFeed(Long userSeq) {
        queryFactory.delete(feedLike)
                .where(feedLike.feed.writer.seq.eq(userSeq));
    }

    @Override
    public void deleteMyScrapedFeed(Long userSeq) {
        queryFactory.delete(scrap)
                .where(scrap.feed.writer.seq.eq(userSeq));
    }
}
