package com.d205.sdutyplus.domain.task.controller;

import com.d205.sdutyplus.domain.statistics.service.DailyStatisticsService;
import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.task.dto.TaskPostDto;
import com.d205.sdutyplus.domain.task.service.TaskService;
import com.d205.sdutyplus.domain.user.service.UserService;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static com.d205.sdutyplus.global.response.ResponseCode.*;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;
    private final DailyStatisticsService dailyStatisticsService;

    @ApiOperation(value = "테스크 등록")
    @PostMapping("")
    public ResponseEntity<ResponseDto> createTask(@RequestBody TaskPostDto taskPostDto){
        TaskDto taskDto = taskService.createTask(taskPostDto);
        dailyStatisticsService.getReportContinuous(taskDto);
        dailyStatisticsService.updateDailyStudy(taskDto);
        return ResponseEntity.ok().body(ResponseDto.of(CREATE_TASK_SUCCESS, taskDto));
    }

    @ApiOperation(value = "테스크 상세 조회")
    @GetMapping("/{task_seq}")
    public ResponseEntity<ResponseDto> getTaskDetail(@PathVariable(value = "task_seq") Long taskSeq){
        TaskDto taskDto = taskService.getTaskDetail(taskSeq);
        return ResponseEntity.ok().body(ResponseDto.of(GET_TASK_DETAIL_SUCCESS, taskDto));
    }

    @ApiOperation(value = "테스크 수정")
    @PutMapping("/{task_seq}")
    public ResponseEntity<ResponseDto> updateTask(@PathVariable(value="task_seq") Long taskSeq, @RequestBody TaskDto taskDto){
        taskService.updateTask(taskSeq, taskDto);
        return ResponseEntity.ok().body(ResponseDto.of(UPDATE_TASK_SUCCESS));
    }

    @ApiOperation(value = "테스크 삭제")
    @DeleteMapping("/{task_seq}")
    public ResponseEntity<ResponseDto> deleteTask(@PathVariable(value="task_seq") Long taskSeq){
        taskService.deleteTask(taskSeq);
        return ResponseEntity.ok().body(ResponseDto.of(DELETE_TASK_SUCCESS));
    }

}
