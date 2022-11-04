package com.d205.sdutyplus.domain.user.controller;

import com.d205.sdutyplus.domain.user.dto.UserDto;
import com.d205.sdutyplus.domain.user.dto.UserRegDto;
import com.d205.sdutyplus.domain.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@Api(tags = "유저 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @ApiOperation(value="회원 프로필 저장")
    @PostMapping("/reg")
    public ResponseEntity<?> userRegData(@RequestBody UserRegDto userRegDto){

        UserDto userDto = userService.userRegData(new Long(1), userRegDto);

        return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
    }
}
