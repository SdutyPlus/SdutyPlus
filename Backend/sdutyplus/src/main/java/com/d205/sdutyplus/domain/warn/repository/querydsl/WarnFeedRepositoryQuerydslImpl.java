package com.d205.sdutyplus.domain.warn.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.d205.sdutyplus.domain.warn.entity.QWarnFeed.warnFeed;

@Repository
@RequiredArgsConstructor
public class WarnFeedRepositoryQuerydslImpl implements WarnFeedRepositoryQuerydsl {
    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteMyWarnedFeedByUserSeq(Long userSeq) {
        queryFactory.delete(warnFeed)
                .where(warnFeed.feed.writerSeq.eq(userSeq));
    }
}
