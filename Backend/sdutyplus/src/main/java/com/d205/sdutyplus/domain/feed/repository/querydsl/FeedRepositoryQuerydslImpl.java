package com.d205.sdutyplus.domain.feed.repository.querydsl;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.dto.QFeedResponseDto;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.global.entity.Job;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static com.d205.sdutyplus.domain.feed.entity.QFeed.feed;
import static com.d205.sdutyplus.domain.feed.entity.QFeedLike.feedLike;
import static com.d205.sdutyplus.domain.feed.entity.QScrap.scrap;
import static com.d205.sdutyplus.domain.off.entity.QOffFeed.offFeed;
import static com.d205.sdutyplus.domain.warn.entity.QWarnFeed.warnFeed;

@RequiredArgsConstructor
public class FeedRepositoryQuerydslImpl implements FeedRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FeedResponseDto> findAllFeedPage(Long userSeq, Pageable pageable) {
        QueryResults<FeedResponseDto> result = queryFactory.select(new QFeedResponseDto(
                                feed.seq,
                                feed.writer,
                                feed.imgUrl,
                                feed.content,
                                feed.feedLikes.size(),
                                feed.scraps.size(),
                                isExistFeedLikeWhereFeedEqAndUserEq(userSeq),
                                isExistFeedScrapWhereFeedEqAndUserEq(userSeq)
                        )
                ).from(feed)
                .where(
                        feed.banYN.eq(false)
                        .and(feed.seq.notIn(getOffFeedByUserSeq(userSeq)))
                        .and(feed.seq.notIn(getWarnFeedByUserSeq(userSeq)))
                )
                .orderBy(feed.seq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    public Optional<FeedResponseDto> findFeedBySeq(Long userSeq, Long feedSeq){
        return Optional.ofNullable(queryFactory.select(new QFeedResponseDto(
                                feed.seq,
                                feed.writer,
                                feed.imgUrl,
                                feed.content,
                                feed.feedLikes.size(),
                                feed.scraps.size(),
                                isExistFeedLikeWhereFeedEqAndUserEq(userSeq),
                                isExistFeedScrapWhereFeedEqAndUserEq(userSeq)
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
                                feed.scraps.size(),
                                isExistFeedLikeWhereFeedEqAndUserEq(writerSeq),
                                isExistFeedScrapWhereFeedEqAndUserEq(writerSeq)
                        )
                ).from(feed)
                .where(feed.writer.seq.eq(writerSeq))
                .orderBy(feed.seq.desc())
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
                                scrap.feed.scraps.size(),
                                isExistFeedLikeWhereFeedEqAndUserEq(userObject.getSeq()),
                                isExistFeedScrapWhereFeedEqAndUserEq(userObject.getSeq())
                        )
                )
                .from(scrap)
                .where(scrap.user.eq(userObject))
                .orderBy(scrap.feed.seq.desc())
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
                                feed.scraps.size(),
                                isExistFeedLikeWhereFeedEqAndUserEq(userSeq),
                                isExistFeedScrapWhereFeedEqAndUserEq(userSeq)
                        )
                )
                .from(feed)
                .where(
                        feed.writer.job.eq(jobObject)
                        .and(
                                feed.banYN.eq(false)
                                .and(feed.seq.notIn(getOffFeedByUserSeq(userSeq)))
                                .and(feed.seq.notIn(getWarnFeedByUserSeq(userSeq)))
                        )

                )
                .orderBy(feed.seq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<FeedResponseDto> findAllWarnFeedPage(Long userSeq, Pageable pageable) {
        QueryResults<FeedResponseDto> result = queryFactory
                .select(
                        new QFeedResponseDto(
                                warnFeed.feed.seq,
                                warnFeed.feed.writer,
                                warnFeed.feed.imgUrl,
                                warnFeed.feed.content,
                                warnFeed.feed.feedLikes.size(),
                                warnFeed.feed.scraps.size(),
                                isExistFeedLikeWhereFeedEqAndUserEq(userSeq),
                                isExistFeedScrapWhereFeedEqAndUserEq(userSeq)
                        )
                )
                .from(warnFeed)
                .orderBy(warnFeed.feed.seq.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression isExistFeedLikeWhereFeedEqAndUserEq(Long userSeq){
        return JPAExpressions
                .selectFrom(feedLike)
                .where(feedLike.feed.seq.eq(feed.seq).and(feedLike.user.seq.eq(userSeq)))
                .exists();
    }

    private BooleanExpression isExistFeedScrapWhereFeedEqAndUserEq(Long userSeq){
        return JPAExpressions
                .selectFrom(scrap)
                .where(scrap.feed.seq.eq(feed.seq).and(scrap.user.seq.eq(userSeq)))
                .exists();
    }

    private JPQLQuery<Long> getWarnFeedByUserSeq(Long userSeq){
        return JPAExpressions.select(warnFeed.feed.seq)
                .where(warnFeed.user.seq.eq(userSeq)).from(warnFeed);
    }

    private JPQLQuery<Long> getOffFeedByUserSeq(Long userSeq){
        return JPAExpressions.select(offFeed.feed.seq)
                .where(offFeed.user.seq.eq(userSeq)).from(offFeed);
    }

}
