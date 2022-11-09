package com.d205.sdutyplus.domain.task.controller;

import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.task.dto.TaskResponseDto;
import com.d205.sdutyplus.domain.task.dto.TaskUpdateDto;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.domain.task.service.TaskService;
import com.d205.sdutyplus.domain.user.service.UserService;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static com.d205.sdutyplus.global.response.ResponseCode.*;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    @ApiOperation(value = "테스크 등록")
    @PostMapping("")
    public ResponseEntity<?> createTask(@ApiIgnore Authentication auth, @RequestBody TaskDto taskRequestDto){
        Long userSeq = (Long)auth.getPrincipal();
        Task registedTask = taskService.createTask(userSeq, taskRequestDto);
        userService.getReportContinuous(userSeq, registedTask);


        return ResponseEntity.ok().body(ResponseDto.of(CREATE_TASK_SUCCESS, registedTask));
    }

    @ApiOperation(value = "테스크 상세 조회")
    @GetMapping("/{task_seq}")
    public ResponseEntity<?> getTaskDetail(@PathVariable(value = "task_seq") Long taskSeq){
        TaskResponseDto taskResponseDto = taskService.getTaskDetail(taskSeq);
        return ResponseEntity.ok().body(ResponseDto.of(GET_TASK_DETAIL_SUCCESS, taskResponseDto));
    }

    @ApiOperation(value = "테스크 수정")
    @PutMapping("/{task_seq}")
    public ResponseEntity<?> updateTask(@PathVariable(value="task_seq") Long taskSeq, @RequestBody TaskUpdateDto taskUpdateDto){
        taskService.updateTask(taskSeq, taskUpdateDto);
        return ResponseEntity.ok().body(ResponseDto.of(UPDATE_TASK_SUCCESS));
    }

    @ApiOperation(value = "테스크 삭제")
    @DeleteMapping("/{task_seq}")
    public ResponseEntity<?> deleteTask(@PathVariable(value="task_seq") Long taskSeq){
        taskService.deleteTask(taskSeq);
        return ResponseEntity.ok().body(ResponseDto.of(DELETE_TASK_SUCCESS));
    }

    @ApiOperation(value = "데일리 테스크 총 시간 조회")
    @GetMapping("/time/{date}")
    public ResponseEntity<?> getDailyTaskTotalTime(@PathVariable String date){
        return null;
//        return ResponseEntity.ok().body(ResponseDto.of());
    }
}
