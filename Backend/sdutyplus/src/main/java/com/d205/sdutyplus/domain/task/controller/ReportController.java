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

import static com.d205.sdutyplus.global.response.ResponseCode.GET_DAILYTASK_SUCCESS;

@RestController
@RequestMapping("/task/report")
@RequiredArgsConstructor
public class ReportController {

    private final TaskService taskService;

    @ApiOperation(value = "데일리 테스크 조회(리포트조회)")
    @GetMapping("/{date}")
    public ResponseEntity<?> getDailyTask(@PathVariable String date){
        ReportResponseDto reportResponseDto = taskService.getTaskByDate(new Long(1), date);
        return ResponseEntity.ok().body(ResponseDto.of(GET_DAILYTASK_SUCCESS, reportResponseDto));
    }
}
