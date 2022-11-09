package com.d205.sdutyplus.domain.user.exception;

import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.BusinessException;

public class NicknameAlreadyExistException extends BusinessException {
    public NicknameAlreadyExistException() {
        super(ErrorCode.USERNAME_ALREADY_EXIST);
    }

}