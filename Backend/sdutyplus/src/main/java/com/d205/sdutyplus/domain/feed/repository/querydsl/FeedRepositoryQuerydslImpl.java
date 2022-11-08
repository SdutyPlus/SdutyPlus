package com.d205.sdutyplus.domain.feed.repository.querydsl;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.dto.QFeedResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.d205.sdutyplus.domain.feed.entity.QFeed.feed;

@Repository
@RequiredArgsConstructor
public class FeedRepositoryQuerydslImpl implements FeedRepositoryQuerydsl {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<FeedResponseDto> findAllFeed() {
        return queryFactory.select(new QFeedResponseDto(
                    feed.seq,
                    feed.writerSeq,
                    feed.imgUrl,
                    feed.content
                )
        ).from(feed)
                .where(feed.banYN.eq(false))
                .fetch();
//        return null;
    }
}
