package com.d205.sdutyplus.domain.off.service;

import com.d205.sdutyplus.domain.off.entity.OffUser;
import com.d205.sdutyplus.domain.off.exception.OffMyselfFailException;
import com.d205.sdutyplus.domain.off.repository.OffRepository;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.EntityAlreadyExistException;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.d205.sdutyplus.global.error.ErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class OffService {
    private final UserRepository userRepository;
    private final OffRepository offRepository;

    public boolean userOff(Long fromUserSeq, Long toUserSeq) {
        final User fromUser = userRepository.findBySeq(fromUserSeq)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        final User toUser = userRepository.findBySeq(toUserSeq)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        // 나 자신을 차단
        if (fromUserSeq.equals(toUserSeq)){
            throw new OffMyselfFailException();
        }

        // 이미 차단한 유저
        if (offRepository.existsByFromUserSeqAndToUserSeq(fromUserSeq, toUserSeq)){
            throw new EntityAlreadyExistException(ErrorCode.OFF_ALREADY_EXIST);
        }

        final OffUser offUser = new OffUser(fromUser, toUser);
        offRepository.save(offUser);

        return true;
    }
}
