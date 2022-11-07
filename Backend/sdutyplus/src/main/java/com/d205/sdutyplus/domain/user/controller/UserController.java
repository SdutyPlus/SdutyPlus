package com.d205.sdutyplus.domain.user.controller;

import com.d205.sdutyplus.domain.user.dto.UserDto;
import com.d205.sdutyplus.domain.user.dto.UserProfileDto;
import com.d205.sdutyplus.domain.user.dto.UserRegDto;
import com.d205.sdutyplus.domain.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            @ApiResponse(code = 200, message = "회원 정보를 등록하였습니다."),
            @ApiResponse(code = 400, message = "유효하지 않은 입력입니다.\n"
                    + "입력 타입이 유효하지 않습니다."),
            @ApiResponse(code = 401, message = "로그인이 필요한 화면입니다.")
    })
    @PostMapping("/reg")
    public ResponseEntity<?> userRegData(@RequestBody UserRegDto userRegDto){

        UserDto userDto = userService.userRegData(new Long(1), userRegDto);

        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }

    @ApiOperation(value = "회원 프로필 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원 정보를 조회합니다."),
            @ApiResponse(code = 401, message = "로그인이 필요한 화면입니다.")
    })
    @GetMapping("/{userSeq}")
    public ResponseEntity<?> getUserProfile(@PathVariable Long userSeq){

        UserProfileDto userProfileDto = userService.getUserProfile(userSeq);

        return new ResponseEntity<UserProfileDto>(userProfileDto, HttpStatus.OK);
    }

    @ApiOperation(value = "별명 중복 검사")
    @ApiResponses({
            @ApiResponse(code = 200, message = "사용 가능한 닉네임 입니다."),
            @ApiResponse(code = 400, message = "이미 존재하는 닉네임 입니다."),
            @ApiResponse(code = 401, message = "로그인이 필요한 화면입니다.")
    })
    @GetMapping("/check")
    public ResponseEntity<?> checkNickname(@RequestBody String nickname){



        return null;
    }
}
