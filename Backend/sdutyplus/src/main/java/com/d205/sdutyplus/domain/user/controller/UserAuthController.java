package com.d205.sdutyplus.domain.user.controller;


import com.d205.sdutyplus.domain.jwt.dto.JwtDto;
import com.d205.sdutyplus.domain.user.entity.SocialType;
import com.d205.sdutyplus.domain.user.service.UserAuthService;
import com.d205.sdutyplus.domain.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping("/naver/login")
    public ResponseEntity<?> naverLogin(@RequestBody String token){
        Map<String, Object> userInfo = userAuthService.getNaverUserInfo(token);
        if(userInfo!=null) {
            String email = userInfo.get("email").toString();
            JwtDto jwtDto = userAuthService.loginUser(email, SocialType.NAVER);
            if(jwtDto!=null) {
                return new ResponseEntity<JwtDto>(jwtDto, HttpStatus.OK);
            }
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value="카카오 로그인")
    @PostMapping("/kakao/login")
    public ResponseEntity<?> kakaoLogin(@RequestBody String token){
        Map<String, Object> userInfo = userAuthService.getKakaoUserInfo(token);
        if(userInfo!=null) {
            String email = userInfo.get("email").toString();
            JwtDto jwtDto = userAuthService.loginUser(email, SocialType.KAKAO);

            System.out.println(jwtDto);

            if(jwtDto!=null) {
                return new ResponseEntity<JwtDto>(jwtDto, HttpStatus.OK);
            }
        }
        return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
    }
}
