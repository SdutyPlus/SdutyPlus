package com.d205.sdutyplus.domain.feed.repository;

import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.feed.entity.FeedLike;
import com.d205.sdutyplus.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {
    Optional<FeedLike> findByUserAndFeed(User user, Feed feed);
    boolean existsByUserSeqAndFeedSeq(Long userSeq, Long feedSeq);
    void deleteAllByUserSeq(Long userSeq);
    void deleteAllByFeedSeq(Long feedSeq);
}
