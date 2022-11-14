package com.d205.sdutyplus.domain.off.repository.queyrdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.d205.sdutyplus.domain.off.entity.QOffFeed.offFeed;

@Repository
@RequiredArgsConstructor
public class OffFeedRepositoryQuerydlImpl implements OffFeedRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    @Override
    public void deleteMyOffedFeedByUserSeq(Long userSeq) {
        queryFactory.delete(offFeed)
                .where(offFeed.feed.writerSeq.eq(userSeq));
    }
}
