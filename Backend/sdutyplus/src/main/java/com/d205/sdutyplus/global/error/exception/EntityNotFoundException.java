package com.d205.sdutyplus.global.error.exception;

import com.d205.sdutyplus.global.error.ErrorCode;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(ErrorCode errorCode){
        super(errorCode.getMessage());
    }
}
