package com.d205.sdutyplus.global.error.exception;

import com.d205.sdutyplus.global.error.ErrorCode;

public class NotSupportedImageTypeException extends BusinessException{
    public NotSupportedImageTypeException(){
        super(ErrorCode.IMAGE_TYPE_NOT_SUPPORT);
    }
}
