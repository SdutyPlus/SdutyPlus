package com.d205.sdutyplus.domain.warn.exception;

import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.BusinessException;

public class WarnMyselfFailException extends BusinessException {

    public WarnMyselfFailException(){
        super(ErrorCode.WARN_MYSELF_FAIL);
    }
}
