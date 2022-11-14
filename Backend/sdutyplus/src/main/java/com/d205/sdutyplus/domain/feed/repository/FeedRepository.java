package com.d205.sdutyplus.domain.feed.repository;

import com.d205.sdutyplus.domain.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {
    void deleteAllByWriterSeq(Long userSeq);

}
