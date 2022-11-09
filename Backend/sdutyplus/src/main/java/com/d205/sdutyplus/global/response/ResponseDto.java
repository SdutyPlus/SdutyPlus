package com.d205.sdutyplus.global.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@ApiModel(description = "결과 응답 데이터")
@Getter
public class ResponseDto {

    @ApiModelProperty(value = "Http 상태 코드")
    private int status;
    @ApiModelProperty(value = "Business 상태 코드")
    private String code;
    @ApiModelProperty(value = "응답 메세지")
    private String message;
    @ApiModelProperty(value = "응답 데이터")
    private Object data;

    public ResponseDto(ResponseCode responseCode, Object data) {
        this.status = responseCode.getStatus();
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    //전송할 데이터가 있는 경우
    public static ResponseDto of(ResponseCode responseCode, Object data) {
        return new ResponseDto(responseCode, data);
    }

    //전송할 데이터가 없는 경우
    public static ResponseDto of(ResponseCode responseCode) {
        return new ResponseDto(responseCode, "");
    }
}
