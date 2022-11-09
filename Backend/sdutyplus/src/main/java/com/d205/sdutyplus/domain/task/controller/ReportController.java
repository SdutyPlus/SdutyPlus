package com.d205.sdutyplus.domain.task.controller;

import com.d205.sdutyplus.domain.task.dto.ReportResponseDto;
import com.d205.sdutyplus.domain.task.service.TaskService;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.d205.sdutyplus.global.response.ResponseCode.GET_REPORT_SUCCESS;
import static com.d205.sdutyplus.global.response.ResponseCode.GET_REPORT_TOTALTIME_SUCCESS;

@RestController
@RequestMapping("/task/report")
@RequiredArgsConstructor
public class ReportController {

    private final TaskService taskService;

    @ApiOperation(value = "데일리 테스크 조회(리포트조회)")
    @GetMapping("/{date}")
    public ResponseEntity<?> getReport(@PathVariable String date){
        ReportResponseDto reportResponseDto = taskService.getDailyReport(new Long(1), date);
        return ResponseEntity.ok().body(ResponseDto.of(GET_REPORT_SUCCESS, reportResponseDto));
    }

    @ApiOperation(value = "리포트 총 시간 조회")
    @GetMapping("/time/{date}")
    public ResponseEntity<ResponseDto> getReportTotalTime(@PathVariable String date){
        String totalTime = taskService.getReportTotalTime(1L, date);
        return ResponseEntity.ok().body(ResponseDto.of(GET_REPORT_TOTALTIME_SUCCESS, totalTime));
    }
}
