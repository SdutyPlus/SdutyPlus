package com.d205.sdutyplus.domain.user.exception;

import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.BusinessException;

public class UserNotLoginException extends BusinessException {
    public UserNotLoginException() {
        super(ErrorCode.AUTHENTICATION_FAIL);
    }
}
