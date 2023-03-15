package com.d205.sdutyplus.domain.task.controller;

import com.d205.sdutyplus.domain.task.dto.ReportDateResponseDto;
import com.d205.sdutyplus.domain.task.dto.ReportDto;
import com.d205.sdutyplus.domain.task.service.TaskService;
import com.d205.sdutyplus.global.response.ResponseDto;
import com.d205.sdutyplus.util.TimeFormatter;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.d205.sdutyplus.global.response.ResponseCode.*;

@RestController
@RequestMapping("/task/report")
@RequiredArgsConstructor
public class ReportController {

    private final TaskService taskService;

    @ApiOperation(value = "데일리 테스크 조회(리포트조회)")
    @GetMapping("/{date}")
    public ResponseEntity<ResponseDto> getReport(@PathVariable String date){
        ReportDto reportResponseDto = taskService.getDailyReport(date);
        return ResponseEntity.ok().body(ResponseDto.of(GET_REPORT_SUCCESS, reportResponseDto));
    }

    @ApiOperation(value = "리포트 오늘 공부한 총 시간 조회")
    @GetMapping("/time")
    public ResponseEntity<ResponseDto> getReportTotalTime(){
        String totalTime = taskService.getReportTotalTime(TimeFormatter.LocalDateToString(TimeFormatter.getTodayDate()));
        return ResponseEntity.ok().body(ResponseDto.of(GET_REPORT_TOTALTIME_SUCCESS, totalTime));
    }

    @ApiOperation(value = "리포트 유저의 공부한 날짜 조회")
    @GetMapping("/date")
    public ResponseEntity<ResponseDto> getReportDate(){
        final ReportDateResponseDto reportDateResponseDto = new ReportDateResponseDto(taskService.getReportDateByOwnerSeq());
        return ResponseEntity.ok().body(ResponseDto.of(GET_REPORT_ALL_DATE_SUCCESS, reportDateResponseDto));
    }

}
