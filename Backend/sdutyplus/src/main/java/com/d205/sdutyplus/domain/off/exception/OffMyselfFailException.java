package com.d205.sdutyplus.domain.off.exception;

import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.BusinessException;

public class OffMyselfFailException extends BusinessException {

    public OffMyselfFailException(){
        super(ErrorCode.OFF_MYSELF_FAIL);
    }
}
