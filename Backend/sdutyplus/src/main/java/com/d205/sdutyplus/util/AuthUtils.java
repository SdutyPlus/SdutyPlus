package com.d205.sdutyplus.util;

import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.exception.UserNotLoginException;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.d205.sdutyplus.global.error.ErrorCode.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class AuthUtils {

    private final UserRepository userRepository;

    public User getLoginUser() {
        final Long userSeq = (Long)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final User loginUser = userRepository.findBySeq(userSeq)
                .orElseThrow(() -> new EntityNotFoundException(USER_NOT_FOUND));
        return loginUser;
    }

    public Long getLoginUserSeq() {
        try {
            final Long userSeq = (Long)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userSeq;
        } catch (Exception e) {
            throw new UserNotLoginException();
        }
    }
}
