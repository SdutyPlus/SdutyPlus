package com.d205.sdutyplus.domain.task.exception;

import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.BusinessException;

public class SubTaskCntLimitException extends BusinessException {

    public SubTaskCntLimitException() {
        super(ErrorCode.SUBTASK_CNT_EXCEEDED_LIMIT);
    }

}
