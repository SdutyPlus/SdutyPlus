package com.d205.sdutyplus.domain.feed.repository.querydsl;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.global.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedRepositoryQuerydsl {
    List<FeedResponseDto> findAllFeeds(Long userSeq);
    Page<FeedResponseDto> findMyFeedPage(Long writerSeq, Pageable pageable);
    Page<FeedResponseDto> findScrapFeedPage(User user, Pageable pageable);
    Page<FeedResponseDto> findFilterFeedPage(Long userSeq, Job jobObject, Pageable pageable);
    void deleteMyLikedFeed(Long userSeq);
    void deleteMyScrapedFeed(Long userSeq);
}
