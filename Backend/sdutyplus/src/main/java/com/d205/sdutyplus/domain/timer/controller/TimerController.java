package com.d205.sdutyplus.domain.timer.controller;

import com.d205.sdutyplus.global.enums.TimeEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/timer")
public class TimerController {

    @ApiOperation(value = "서버 현재 시간 조회")
    @GetMapping("")
    public ResponseEntity<?> getServerTime(){
        LocalDateTime datetime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeEnum.dateTimeFormat);
        String now = datetime.format(formatter);
        return new ResponseEntity<String>(now.toString(), HttpStatus.OK);
    }
}
