package com.d205.sdutyplus.domain.feed.repository.querydsl;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedRepositoryQuerydsl {
    List<FeedResponseDto> findAllFeeds();
    public Page<FeedResponseDto> findMyFeedPage(Long writerSeq, Pageable pageable);
    public Page<FeedResponseDto> findScrapFeedPage(User user, Pageable pageable);
}
