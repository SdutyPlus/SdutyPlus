package com.d205.sdutyplus.domain.feed.exception;

import com.d205.sdutyplus.global.error.ErrorCode;
import com.d205.sdutyplus.global.error.exception.BusinessException;

public class CannotDeleteFeedException extends BusinessException {

    public CannotDeleteFeedException() {
        super(ErrorCode.CAN_NOT_DELETE_FEED);
    }
}
