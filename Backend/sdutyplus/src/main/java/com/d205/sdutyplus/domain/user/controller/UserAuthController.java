package com.d205.sdutyplus.domain.user.controller;


import com.d205.sdutyplus.domain.jwt.dto.JwtDto;
import com.d205.sdutyplus.domain.user.dto.UserLoginDto;
import com.d205.sdutyplus.domain.user.entity.SocialType;
import com.d205.sdutyplus.domain.user.service.UserAuthService;
import com.d205.sdutyplus.domain.user.service.UserService;
import com.d205.sdutyplus.global.response.ResponseCode;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
    public ResponseEntity<ResponseDto> deleteUser(@ApiIgnore Authentication auth){
        Long userSeq = (Long)auth.getPrincipal();

        boolean success = userAuthService.deleteUser(userSeq);

        if (success) {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.DELETE_SUCCESS));
        } else {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.DELETE_FAIL));
        }
    }
}
