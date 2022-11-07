package com.d205.sdutyplus.domain.feed.repository.querydsl;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;

import java.util.List;

public interface FeedRepositoryQuerydsl {
    List<FeedResponseDto> findAllFeed();
}
