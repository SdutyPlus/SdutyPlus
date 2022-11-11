package com.d205.sdutyplus.domain.warn.controller;

import com.d205.sdutyplus.domain.warn.service.WarnService;

import com.d205.sdutyplus.global.response.ResponseCode;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import lombok.RequiredArgsConstructor;

import springfox.documentation.annotations.ApiIgnore;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "유저 신고 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/warning")
public class WarnController {

    private final WarnService warnService;

    @ApiOperation(value = "유저 신고")
    @ApiResponses({
            @ApiResponse(code = 200, message = "W001 - 신고 완료."),
            @ApiResponse(code = 400, message = "W001 - 이미 신고한 유저입니다.\n"
                    + "W002 - 자시 자신을 신고 할 수 없습니다." ),
            @ApiResponse(code = 401, message = "U003 - 로그인이 필요한 화면입니다.")
    })
    @PostMapping("/user/{toUserSeq}")
    public ResponseEntity<ResponseDto> userWarn(@ApiIgnore Authentication auth, @PathVariable Long toUserSeq) {
        Long fromUserSeq = (Long)auth.getPrincipal();

        final boolean success = warnService.userWarn(fromUserSeq, toUserSeq);
        if (success) {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.WARN_SUCCESS, success));
        } else {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.WARN_FAIL, success));
        }
    }

    @ApiOperation(value = "피드 신고")
    @ApiResponses({
            @ApiResponse(code = 200, message = "신고 완료."),
            @ApiResponse(code = 400, message = "이미 신고한 피드입니다.\n"),
            @ApiResponse(code = 401, message = "로그인이 필요한 화면입니다.")
    })
    @PostMapping("/feed/{feedSeq}")
    public ResponseEntity<ResponseDto> feedWarn(@ApiIgnore Authentication auth, @PathVariable Long feedSeq) {
        Long userSeq = (Long)auth.getPrincipal();

        final boolean success = warnService.feedWarn(userSeq, feedSeq);
        if (success) {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.WARN_SUCCESS, success));
        } else {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.WARN_FAIL, success));
        }
    }
}
