package com.d205.sdutyplus.domain.admin.service;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.dto.PagingResultDto;
import com.d205.sdutyplus.domain.feed.repository.FeedRepository;
import com.d205.sdutyplus.domain.feed.repository.querydsl.FeedRepositoryQuerydsl;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.querydsl.UserRepositoryQuerydsl;
import com.d205.sdutyplus.domain.warn.dto.WarnUserDto;
import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.EntityAlreadyExistException;
import com.d205.sdutyplus.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final FeedRepository feedRepository;
    private final UserRepositoryQuerydsl userRepositoryQuerydsl;
    private final AuthUtils authUtils;

    @Transactional
    public PagingResultDto getWarnFeed(Pageable pageable){
        final Long userSeq = authUtils.getLoginUserSeq();
        final Page<FeedResponseDto> warnFeeds = feedRepository.findAllWarnFeedPage(userSeq, pageable);
        final PagingResultDto pagingResultDto = new PagingResultDto<FeedResponseDto>(pageable.getPageNumber(), warnFeeds.getTotalPages() - 1, warnFeeds.getContent());

        return pagingResultDto;
    }

    @Transactional
    public PagingResultDto getWarnUser(Pageable pageable){
        final Page<WarnUserDto> warnUsers = userRepositoryQuerydsl.findAllWarnUserPage(pageable);
        final PagingResultDto pagingResultDto = new PagingResultDto<WarnUserDto>(pageable.getPageNumber(), warnUsers.getTotalPages() - 1, warnUsers.getContent());

        return pagingResultDto;
    }

    @Transactional
    public boolean banWarnUser(Long warnUserSeq){
        final User warnUser = authUtils.getLoginUser(warnUserSeq);

        if (warnUser.isBanYN()) {
            throw new EntityAlreadyExistException(ErrorCode.BAN_USER_ALREADY_EXIST);
        }

        warnUser.setBanYN(true);
        return true;
    }
}
