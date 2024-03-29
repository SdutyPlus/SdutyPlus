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

import javax.transaction.Transactional;

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

    @Transactional
    public boolean userOff(Long toUserSeq) {
        final User fromUser = authUtils.getLoginUser();
        final User toUser = userRepository.findBySeq(toUserSeq)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        if (fromUser.getSeq().equals(toUserSeq)){
            throw new OffMyselfFailException();
        }

        if (offUserRepository.existsByFromUserSeqAndToUserSeq(fromUser.getSeq(), toUserSeq)){
            throw new EntityAlreadyExistException(ErrorCode.OFF_ALREADY_EXIST);
        }

        final OffUser offUser = new OffUser(fromUser, toUser);
        offUserRepository.save(offUser);

        return true;
    }

    @Transactional
    public boolean feedOff(Long feedSeq) {
        final User user = authUtils.getLoginUser();
        final Feed feed = feedRepository.findById(feedSeq)
                .orElseThrow(() -> new EntityNotFoundException(FEED_NOT_FOUND));

        if (offFeedRepository.existsByFeedSeqAndUserSeq(feedSeq, user.getSeq())){
            throw new EntityAlreadyExistException(ErrorCode.OFF_ALREADY_EXIST);
        }

        final OffFeed offFeed = new OffFeed(user, feed);
        offFeedRepository.save(offFeed);

        return true;
    }

    @Transactional
    public void deleteAllFeedOffByUserSeq(Long userSeq){
        offFeedRepository.deleteAllByUserSeq(userSeq);
    }

}
