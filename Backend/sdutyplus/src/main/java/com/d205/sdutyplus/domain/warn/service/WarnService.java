package com.d205.sdutyplus.domain.warn.service;

import static com.d205.sdutyplus.global.error.ErrorCode.*;

import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.feed.repository.FeedRepository;
import com.d205.sdutyplus.domain.warn.entity.WarnFeed;
import com.d205.sdutyplus.domain.warn.exception.WarnMyselfFailException;
import com.d205.sdutyplus.domain.warn.repository.WarnFeedRepository;
import com.d205.sdutyplus.util.AuthUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.domain.warn.entity.WarnUser;
import com.d205.sdutyplus.domain.warn.repository.WarnUserRepository;
import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.EntityAlreadyExistException;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarnService {

    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final WarnUserRepository warnUserRepository;
    private final WarnFeedRepository warnFeedRepository;
    private final AuthUtils authUtils;

    @Transactional
    public boolean userWarn(Long toUserSeq){
        final Long fromUserSeq = authUtils.getLoginUserSeq();
        final User fromUser = authUtils.getLoginUser(fromUserSeq);
        final User toUser = authUtils.getLoginUser(toUserSeq);

        // 나 자신을 신고
        if (fromUserSeq.equals(toUserSeq)){
            throw new WarnMyselfFailException();
        }

        // 이미 신고한 유저
        if (warnUserRepository.existsByFromUserSeqAndToUserSeq(fromUserSeq, toUserSeq)){
            throw new EntityAlreadyExistException(ErrorCode.WARN_USER_ALREADY_EXIST);
        }

        final WarnUser warnUser = new WarnUser(fromUser, toUser);
        warnUserRepository.save(warnUser);

        return true;
    }

    @Transactional
    public boolean feedWarn(Long feedSeq){
        final Long userSeq = authUtils.getLoginUserSeq();
        final User user = authUtils.getLoginUser(userSeq);
        final Feed feed = feedRepository.findById(feedSeq)
                .orElseThrow(()->new EntityNotFoundException(FEED_NOT_FOUND));

        if(warnFeedRepository.existsByUserAndFeed(user, feed)){
            throw new EntityAlreadyExistException(WARN_FEED_ALREADY_EXIST);
        }

        final WarnFeed warnFeed = new WarnFeed(user, feed);
        warnFeedRepository.save(warnFeed);

        return true;
    }
}
