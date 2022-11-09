package com.d205.sdutyplus.util;

import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.d205.sdutyplus.global.error.ErrorCode.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepository userRepository;

    public User getLoginUser(Long loginUserSeq) {
        final User loginUser = userRepository.findBySeq(loginUserSeq)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));

        return loginUser;
    }
}
