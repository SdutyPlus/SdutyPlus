package com.d205.sdutyplus.domain.warn.repository;

import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.warn.entity.WarnFeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarnFeedRepository extends JpaRepository<WarnFeed, Long>{
    boolean existsByUserAndFeed(User user, Feed feed);
    void deleteAllByUserSeq(Long userSeq);
    void deleteAllByFeedSeq(Long feedSeq);
}
