package com.d205.sdutyplus.global.error.exception;

import com.d205.sdutyplus.global.error.ErrorCode;

public class InvalidInputException extends BusinessException{

    public InvalidInputException() {
        super(ErrorCode.INPUT_VALUE_INVALID);
    }
}
