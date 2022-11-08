package com.d205.sdutyplus.domain.off.controller;

import com.d205.sdutyplus.domain.off.service.OffService;
import com.d205.sdutyplus.global.response.ResponseCode;
import com.d205.sdutyplus.global.response.ResponseDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "유저 차단 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/off")
public class OffController {

    private final OffService offService;

    @ApiOperation(value = "유저 차단")
    @ApiResponses({
            @ApiResponse(code = 200, message = "O001 - 차단 완료."),
            @ApiResponse(code = 400, message = "O001 - 이미 차단한 유저입니다.\n"
                    + "O002 - 자시 자신을 차단 할 수 없습니다." ),
            @ApiResponse(code = 401, message = "U003 - 로그인이 필요한 화면입니다.")
    })
    @PostMapping("/user/{toUserSeq}")
    public ResponseEntity<ResponseDto> userWarn(@ApiIgnore Authentication auth, @PathVariable Long toUserSeq) {
        long fromUserSeq = (int)auth.getPrincipal();

        final boolean success = offService.userOff(fromUserSeq, toUserSeq);
        if (success) {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.OFF_SUCCESS, success));
        } else {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.OFF_FAIL, success));
        }
    }
}
