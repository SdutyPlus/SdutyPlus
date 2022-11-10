package com.d205.sdutyplus.domain.task.exception;

import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.BusinessException;

public class TimeDuplicateException extends BusinessException {

    public TimeDuplicateException() {
        super(ErrorCode.TASK_TIME_DUPLICATE);
    }
}
