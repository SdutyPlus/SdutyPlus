package com.d205.sdutyplus.domain.user.controller;

import com.d205.sdutyplus.domain.user.dto.UserProfileDto;
import com.d205.sdutyplus.domain.user.dto.UserProfileEditDto;
import com.d205.sdutyplus.domain.user.dto.UserRegDto;
import com.d205.sdutyplus.domain.user.dto.UserRegResponseDto;
import com.d205.sdutyplus.domain.user.service.UserService;
import com.d205.sdutyplus.global.response.ResponseDto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static com.d205.sdutyplus.global.response.ResponseCode.*;

@Log4j2
@Api(tags = "유저 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "회원 프로필 저장")
    @ApiResponses({
            @ApiResponse(code = 200, message = "U008 - 회원 프로필을 저장하였습니다."),
            @ApiResponse(code = 401, message = "U003 - 로그인이 필요한 화면입니다.")
    })
    @PostMapping("/reg")
    public ResponseEntity<ResponseDto> userRegData(@ApiIgnore Authentication auth, @RequestBody UserRegDto userRegDto){
        Long userSeq = (Long)auth.getPrincipal();
        UserRegResponseDto result = userService.userRegData(userSeq, userRegDto);

        return ResponseEntity.ok(ResponseDto.of(SAVE_PROFILE_SUCCESS, result));
    }

    @ApiOperation(value = "별명 중복 검사")
    @ApiResponses({
            @ApiResponse(code = 200, message = "U005 - 사용가능한 nickname 입니다.\n"
                    + "U006 - 사용불가능한 nickname 입니다."),
            @ApiResponse(code = 401, message = "U003 - 로그인이 필요한 화면입니다.")
    })
    @GetMapping("/check/{nickname}")
    public ResponseEntity<ResponseDto> checkNicknameDuplicate(@PathVariable String nickname) {
        final boolean check = userService.checkNicknameDuplicate(nickname);
        if (check) {
            return ResponseEntity.ok(ResponseDto.of(CHECK_NICKNAME_GOOD, check));
        } else {
            return ResponseEntity.ok(ResponseDto.of(CHECK_NICKNAME_BAD, check));
        }
    }

    @ApiOperation(value = "회원 프로필 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "U002 - 회원 프로필을 조회하였습니다."),
            @ApiResponse(code = 401, message = "U003 - 로그인이 필요한 화면입니다.")
    })
    @GetMapping
    public ResponseEntity<ResponseDto> getUserProfile(@ApiIgnore Authentication auth){
        Long userSeq = (Long)auth.getPrincipal();
        final UserProfileDto userProfileDto = userService.getUserProfile(userSeq);

        return ResponseEntity.ok(ResponseDto.of(GET_USERPROFILE_SUCCESS, userProfileDto));
    }

    @ApiOperation(value = "회원 프로필 수정")
    @ApiResponses({
            @ApiResponse(code = 200, message = "U004 - 회원 프로필을 수정하였습니다."),
            @ApiResponse(code = 401, message = "U003 - 로그인이 필요한 화면입니다.")
    })
    @PutMapping
    public ResponseEntity<ResponseDto> putUserProfile(@ApiIgnore Authentication auth, @RequestBody UserProfileEditDto userProfileEditDto){
        Long userSeq = (Long)auth.getPrincipal();
        UserRegResponseDto result = userService.userProfileEdit(userSeq, userProfileEditDto);

        return ResponseEntity.ok(ResponseDto.of(EDIT_PROFILE_SUCCESS, result));
    }
}
