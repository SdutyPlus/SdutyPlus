package com.d205.sdutyplus.domain.feed.repository.querydsl;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.dto.QFeedResponseDto;
import com.d205.sdutyplus.domain.user.entity.User;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.d205.sdutyplus.domain.feed.entity.QFeed.feed;
import static com.d205.sdutyplus.domain.feed.entity.QScrap.scrap;
import static com.d205.sdutyplus.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryQuerydslImpl implements FeedRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FeedResponseDto> findAllFeeds() {
        return queryFactory.select(new QFeedResponseDto(
                    feed.seq,
                    feed.writerSeq,
                    feed.imgUrl,
                    feed.content
                )
        ).from(feed)
                .where(feed.banYN.eq(false))
                .fetch();
    }

    @Override
    public List<FeedResponseDto> findMyFeeds(Long writerSeq) {
        return queryFactory.select(new QFeedResponseDto(
                                feed.seq,
                                feed.writerSeq,
                                feed.imgUrl,
                                feed.content
                        )
                ).from(feed)
                .where(feed.writerSeq.eq(writerSeq))
                .fetch();
    }

    //TODO: 제거할 코드
//    @Override
//    public List<FeedResponseDto> findScrapFeeds(User userObject) {
//        return queryFactory
//                .select(
//                        new QFeedResponseDto(
//                                scrap.feed.seq,
//                                scrap.feed.writerSeq,
//                                scrap.feed.imgUrl,
//                                scrap.feed.content
//                        )
//                )
//                .from(scrap)
//                .where(scrap.user.eq(userObject))
//                .fetch();
//    }

    @Override
    public Page<FeedResponseDto> findScrapFeedPage(User userObject, Pageable pageable) {
        //List<FeedResponseDto> feedResponseDtos = findScrapFeeds(userObject);
        //Long cnt = getScrapFeedCount(userObject);
        //return new PageImpl<>(content, pageable, count);
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

    /**
     * private 영역
     * - 기능 모듈화
     */

//    private Long getScrapFeedCount(User userObject){
//        return queryFactory
//                .select(
//                        scrap.count()
//                )
//                .from(scrap)
//                .where(scrap.user.eq(userObject))
//                .fetchOne();
//    }
}
