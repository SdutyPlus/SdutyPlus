package com.d205.sdutyplus.domain.admin.controller;

import com.d205.sdutyplus.domain.admin.service.AdminService;
import com.d205.sdutyplus.global.dto.PagingResultDto;
import com.d205.sdutyplus.global.response.ResponseCode;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Log4j2
@Api(tags = "관리자 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @ApiOperation(value = "신고된 게시물 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "A001 - 신고 게시글 조회가 성공하였습니다."),
            @ApiResponse(code = 401, message = "U003 - 로그인이 필요한 화면입니다.")
    })
    @GetMapping("/warn/feed")
    public ResponseEntity<ResponseDto> getWarnFeed(@PageableDefault Pageable pageable) {
        final PagingResultDto pagingResultDto = adminService.getWarnFeed(pageable);

        return ResponseEntity.ok().body(ResponseDto.of(ResponseCode.GET_WARN_FEED_SUCCESS, pagingResultDto));
    }

    @ApiOperation(value = "신고된 유저 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "A003 - 신고 유저 조회가 성공하였습니다."),
            @ApiResponse(code = 401, message = "U003 - 로그인이 필요한 화면입니다.")
    })
    @GetMapping("/warn/user")
    public ResponseEntity<ResponseDto> getWarnUser(@PageableDefault Pageable pageable) {
        final PagingResultDto pagingResultDto = adminService.getWarnUser(pageable);

        return ResponseEntity.ok().body(ResponseDto.of(ResponseCode.GET_WARN_USER_SUCCESS, pagingResultDto));
    }

    @ApiOperation(value = "신고된 유저 권한 제한")
    @ApiResponses({
            @ApiResponse(code = 200, message = "A005 - 신고 유저 제재가 성공하였습니다."),
            @ApiResponse(code = 401, message = "U003 - 로그인이 필요한 화면입니다.")
    })
    @PutMapping("/warn/user")
    public ResponseEntity<ResponseDto> banWarnUser() {
        final boolean success = adminService.banWarnUser();

        if (success) {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.BAN_WARN_USER_SUCCESS, true));
        } else {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.BAN_WARN_USER_FAIL, false));
        }
    }
}
