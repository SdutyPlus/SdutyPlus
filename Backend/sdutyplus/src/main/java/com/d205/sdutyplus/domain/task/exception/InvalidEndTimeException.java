package com.d205.sdutyplus.domain.task.exception;

import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.BusinessException;

public class InvalidEndTimeException extends BusinessException {

    public InvalidEndTimeException() {
        super(ErrorCode.INVALID_END_TIME);
    }
}
