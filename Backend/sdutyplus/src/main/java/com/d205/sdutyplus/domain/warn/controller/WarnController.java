package com.d205.sdutyplus.domain.warn.controller;

import com.d205.sdutyplus.domain.warn.service.WarnService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/warning")
public class WarnController {

    private final WarnService warnService;

    @ApiOperation(value = "유저 신고")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원을 신고 하였습니다."),
            @ApiResponse(code = 401, message = "로그인이 필요한 화면입니다.")
    })
    @PostMapping("/user/{toUserSeq}")
    public ResponseEntity<?> userWarn(@ApiIgnore Authentication auth, @PathVariable Long toUserSeq) {
        long fromUserSeq = (int)auth.getPrincipal();

        return ResponseEntity.ok(warnService.userWarn(fromUserSeq, toUserSeq));
    }
}
