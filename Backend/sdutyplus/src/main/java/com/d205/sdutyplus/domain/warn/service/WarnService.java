package com.d205.sdutyplus.domain.warn.service;

import static com.d205.sdutyplus.global.error.ErrorCode.*;

import com.d205.sdutyplus.domain.warn.exception.WarnMyselfFailException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.domain.warn.entity.WarnUser;
import com.d205.sdutyplus.domain.warn.repository.WarnRepository;
import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.EntityAlreadyExistException;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarnService {

    private final UserRepository userRepository;
    private final WarnRepository warnRepository;

    public boolean userWarn(Long fromUserSeq, Long toUserSeq){
        final User fromUser = userRepository.findBySeq(fromUserSeq)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        final User toUser = userRepository.findBySeq(toUserSeq)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        // 나 자신을 신고
        if (fromUserSeq.equals(toUserSeq)){
            throw new WarnMyselfFailException();
        }

        // 이미 신고한 유저
        if (warnRepository.existsByFromUserSeqAndToUserSeq(fromUserSeq, toUserSeq)){
            throw new EntityAlreadyExistException(ErrorCode.WARN_ALREADY_EXIST);
        }

        final WarnUser warnUser = new WarnUser(fromUser, toUser);
        warnRepository.save(warnUser);

        return true;
    }
}
