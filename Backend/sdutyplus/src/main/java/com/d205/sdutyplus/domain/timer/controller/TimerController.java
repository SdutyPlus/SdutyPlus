package com.d205.sdutyplus.domain.timer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/timer")
public class TimerController {

    @GetMapping("")
    public ResponseEntity<?> getServerTime(){
        LocalDateTime datetime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String now = datetime.format(formatter);
        return new ResponseEntity<String>(now.toString(), HttpStatus.OK);
    }
}
