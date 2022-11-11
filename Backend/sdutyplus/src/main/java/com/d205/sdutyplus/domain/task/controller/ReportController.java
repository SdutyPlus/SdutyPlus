package com.d205.sdutyplus.domain.task.controller;

import com.d205.sdutyplus.domain.task.dto.ReportDto;
import com.d205.sdutyplus.domain.task.service.TaskService;
import com.d205.sdutyplus.global.response.ResponseDto;
import com.d205.sdutyplus.util.TimeFormatter;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.d205.sdutyplus.global.response.ResponseCode.GET_REPORT_SUCCESS;
import static com.d205.sdutyplus.global.response.ResponseCode.GET_REPORT_TOTALTIME_SUCCESS;

@RestController
@RequestMapping("/task/report")
@RequiredArgsConstructor
public class ReportController {

    private final TaskService taskService;

    @ApiOperation(value = "데일리 테스크 조회(리포트조회)")
    @GetMapping("/{date}")
    public ResponseEntity<ResponseDto> getReport(@ApiIgnore Authentication auth, @PathVariable String date){
        Long userSeq = (Long)auth.getPrincipal();
        ReportDto reportResponseDto = taskService.getDailyReport(userSeq, date);
        return ResponseEntity.ok().body(ResponseDto.of(GET_REPORT_SUCCESS, reportResponseDto));
    }

    @ApiOperation(value = "리포트 오늘 공부한 총 시간 조회")
    @GetMapping("/time")
    public ResponseEntity<ResponseDto> getReportTotalTime(@ApiIgnore Authentication auth){
        Long userSeq = (Long)auth.getPrincipal();
        String totalTime = taskService.getReportTotalTime(userSeq, TimeFormatter.LocalDateToString(TimeFormatter.getTodayDate()));
        return ResponseEntity.ok().body(ResponseDto.of(GET_REPORT_TOTALTIME_SUCCESS, totalTime));
    }
}
