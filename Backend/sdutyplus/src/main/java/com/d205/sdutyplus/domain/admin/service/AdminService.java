package com.d205.sdutyplus.domain.admin.service;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.global.dto.PagingResultDto;
import com.d205.sdutyplus.domain.feed.repository.FeedRepository;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.domain.warn.dto.WarnUserDto;
import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.EntityAlreadyExistException;
import com.d205.sdutyplus.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final AuthUtils authUtils;

    public PagingResultDto getWarnFeed(Pageable pageable){
        final Long userSeq = authUtils.getLoginUserSeq();
        final Page<FeedResponseDto> warnFeeds = feedRepository.findAllWarnFeedPage(userSeq, pageable);
        final PagingResultDto<FeedResponseDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), warnFeeds.getTotalPages() - 1, warnFeeds.getContent());

        return pagingResultDto;
    }

    public PagingResultDto getWarnUser(Pageable pageable){
        final Page<WarnUserDto> warnUsers = userRepository.findAllWarnUserPage(pageable);
        final PagingResultDto<WarnUserDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), warnUsers.getTotalPages() - 1, warnUsers.getContent());

        return pagingResultDto;
    }

    @Transactional
    public boolean banWarnUser(){
        final User warnUser = authUtils.getLoginUser();

        if (warnUser.isBanYN()) {
            throw new EntityAlreadyExistException(ErrorCode.BAN_USER_ALREADY_EXIST);
        }

        warnUser.setBanYN(true);
        return true;
    }
}
