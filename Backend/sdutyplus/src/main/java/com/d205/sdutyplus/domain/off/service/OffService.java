package com.d205.sdutyplus.domain.off.service;

import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.feed.repository.FeedRepository;
import com.d205.sdutyplus.domain.off.entity.OffFeed;
import com.d205.sdutyplus.domain.off.entity.OffUser;
import com.d205.sdutyplus.domain.off.exception.OffMyselfFailException;
import com.d205.sdutyplus.domain.off.repository.OffFeedRepository;
import com.d205.sdutyplus.domain.off.repository.OffUserRepository;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.EntityAlreadyExistException;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.d205.sdutyplus.global.error.ErrorCode.FEED_NOT_FOUND;
import static com.d205.sdutyplus.global.error.ErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class OffService {
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;
    private final OffUserRepository offUserRepository;
    private final OffFeedRepository offFeedRepository;
    private final AuthUtils authUtils;

    public boolean userOff(Long toUserSeq) {
        final Long fromUserSeq = authUtils.getLoginUserSeq();
        final User fromUser = authUtils.getLoginUser(fromUserSeq);
        final User toUser = authUtils.getLoginUser(toUserSeq);

        if (fromUserSeq.equals(toUserSeq)){
            throw new OffMyselfFailException();
        }

        if (offUserRepository.existsByFromUserSeqAndToUserSeq(fromUserSeq, toUserSeq)){
            throw new EntityAlreadyExistException(ErrorCode.OFF_ALREADY_EXIST);
        }

        final OffUser offUser = new OffUser(fromUser, toUser);
        offUserRepository.save(offUser);

        return true;
    }

    public boolean feedOff(Long feedSeq) {
        final Long userSeq = authUtils.getLoginUserSeq();
        final User user = authUtils.getLoginUser(userSeq);
        final Feed feed = feedRepository.findById(feedSeq)
                .orElseThrow(() -> new EntityNotFoundException(FEED_NOT_FOUND));

        if (offFeedRepository.existsByFeedSeqAndUserSeq(feedSeq, userSeq)){
            throw new EntityAlreadyExistException(ErrorCode.OFF_ALREADY_EXIST);
        }

        final OffFeed offFeed = new OffFeed(user, feed);
        offFeedRepository.save(offFeed);

        return true;
    }
}
