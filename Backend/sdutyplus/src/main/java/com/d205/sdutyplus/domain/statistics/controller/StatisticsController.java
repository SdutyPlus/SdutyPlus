package com.d205.sdutyplus.domain.statistics.controller;

import com.d205.sdutyplus.domain.statistics.dto.StatisticsDto;
import com.d205.sdutyplus.domain.statistics.service.DailyStatisticsService;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;

import static com.d205.sdutyplus.global.response.ResponseCode.*;

@Log4j2
@Api(tags = "통계 API")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticsController {

    private final DailyStatisticsService dailyStatisticsService;

    @ApiOperation(value = "회원 통게 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "ST001 - 회원 통계를 조회하였습니다."),
            @ApiResponse(code = 401, message = "U003 - 로그인이 필요한 화면입니다.")
    })
    @GetMapping
    public ResponseEntity<ResponseDto> getUserStatistics() {

        final StatisticsDto statisticsDto = dailyStatisticsService.getUserStatistics();

        return ResponseEntity.ok(ResponseDto.of(GET_STATISTICS_SUCCESS, statisticsDto));
    }
}
