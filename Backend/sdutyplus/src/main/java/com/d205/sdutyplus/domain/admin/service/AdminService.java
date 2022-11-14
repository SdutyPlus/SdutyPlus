package com.d205.sdutyplus.domain.admin.service;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.dto.PagingResultDto;
import com.d205.sdutyplus.domain.feed.repository.querydsl.FeedRepositoryQuerydsl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final FeedRepositoryQuerydsl feedRepositoryQuerydsl;

    @Transactional
    public PagingResultDto getWarnFeed(Pageable pageable){
        final Page<FeedResponseDto> warnFeeds = feedRepositoryQuerydsl.findAllWarnFeedPage(pageable);
        final PagingResultDto pagingResultDto = new PagingResultDto<FeedResponseDto>(pageable.getPageNumber(), warnFeeds.getTotalPages() - 1, warnFeeds.getContent());

        return pagingResultDto;
    }

    @Transactional
    public PagingResultDto getWarnUser(Pageable pageable){
        final Page<FeedResponseDto> warnFeeds = feedRepositoryQuerydsl.findAllWarnFeedPage(pageable);
        final PagingResultDto pagingResultDto = new PagingResultDto<FeedResponseDto>(pageable.getPageNumber(), warnFeeds.getTotalPages() - 1, warnFeeds.getContent());

        return pagingResultDto;
    }
}
