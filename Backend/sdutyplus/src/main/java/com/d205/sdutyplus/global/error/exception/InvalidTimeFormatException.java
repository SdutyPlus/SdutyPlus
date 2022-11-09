package com.d205.sdutyplus.global.error.exception;

import com.d205.sdutyplus.global.error.ErrorCode;

public class InvalidTimeFormatException extends BusinessException{
    public InvalidTimeFormatException() {
        super(ErrorCode.TIME_FORMAT_INVALID);
    }
}
