package com.d205.sdutyplus.domain.user.controller;

import com.d205.sdutyplus.domain.user.dto.UserLoginDto;
import com.d205.sdutyplus.domain.user.entity.SocialType;
import com.d205.sdutyplus.domain.user.service.UserAuthService;
import com.d205.sdutyplus.global.response.ResponseCode;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@Api(tags = "유저 인증 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserAuthController {

    private final UserAuthService userAuthService;

    @ApiOperation(value="네이버 로그인")
    @ApiResponses({
            @ApiResponse(code = 200, message = "U001 - 로그인에 성공하였습니다."),
            @ApiResponse(code = 401, message = "U005 - 계정 정보가 일치하지 않습니다.")
    })
    @PostMapping("/naver/login")
    public ResponseEntity<ResponseDto> naverLogin(@RequestBody String token){
        Map<String, Object> userInfo = userAuthService.getNaverUserInfo(token);
        if(userInfo!=null) {
            String email = userInfo.get("email").toString();
            UserLoginDto userLoginDto = userAuthService.loginUser(email, SocialType.NAVER);


            if(userLoginDto != null) {
                return ResponseEntity.ok(ResponseDto.of(ResponseCode.LOGIN_SUCCESS, userLoginDto));
            }
        }
        return ResponseEntity.ok(ResponseDto.of(ResponseCode.LOGIN_FAIL));
    }

    @ApiOperation(value="카카오 로그인")
    @ApiResponses({
            @ApiResponse(code = 200, message = "U001 - 로그인에 성공하였습니다."),
            @ApiResponse(code = 401, message = "U005 - 계정 정보가 일치하지 않습니다.")
    })
    @PostMapping("/kakao/login")
    public ResponseEntity<ResponseDto> kakaoLogin(@RequestBody String token){
        Map<String, Object> userInfo = userAuthService.getKakaoUserInfo(token);
        if(userInfo!=null) {
            String email = userInfo.get("email").toString();
            UserLoginDto userLoginDto = userAuthService.loginUser(email, SocialType.KAKAO);


            if(userLoginDto != null) {
                return ResponseEntity.ok(ResponseDto.of(ResponseCode.LOGIN_SUCCESS, userLoginDto));
            }
        }
        return ResponseEntity.ok(ResponseDto.of(ResponseCode.LOGIN_FAIL));
    }

    @ApiOperation(value = "로그아웃")
    @ApiResponses({
            @ApiResponse(code = 401, message = "U005 - 계정 정보가 일치하지 않습니다.")
    })
    @PostMapping(value = "/logout")
    public ResponseEntity<ResponseDto> logout(){
        return null;
    }


    @ApiOperation(value = "회원 탈퇴")
    @ApiResponses({
            @ApiResponse(code = 200, message = "U009 - 회원 탈퇴에 성공하였습니다."),
            @ApiResponse(code = 401, message = "U005 - 계정 정보가 일치하지 않습니다.")
    })
    @DeleteMapping
    public ResponseEntity<ResponseDto> deleteUser(){

        boolean success = userAuthService.deleteUser();

        if (success) {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.DELETE_SUCCESS));
        } else {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.DELETE_FAIL));
        }
    }

    @ApiOperation(value = "토큰 만료 확인")
    @ApiResponses({
            @ApiResponse(code = 200, message = "U011 - 유효한 토큰 입니다."),
            @ApiResponse(code = 401, message = "U005 - 계정 정보가 일치하지 않습니다.")
    })
    @GetMapping("/token")
    public ResponseEntity<ResponseDto> checkTokenExpiration(){
        boolean success = userAuthService.checkTokenExpiration();
        if (success) {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.CHECK_TOKEN_SUCCESS, true));
        } else {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.CHECK_TOKEN_FAIL, false));
        }
    }
}
