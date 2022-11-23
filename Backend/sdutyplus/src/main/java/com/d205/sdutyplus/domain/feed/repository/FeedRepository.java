package com.d205.sdutyplus.domain.feed.repository;

import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.feed.repository.querydsl.FeedRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long>, FeedRepositoryQuerydsl {
    List<Feed> findAllByWriterSeq(Long userSeq);
}
