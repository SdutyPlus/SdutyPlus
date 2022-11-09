package com.d205.sdutyplus.domain.task.controller;

import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.task.dto.TaskPostDto;
import com.d205.sdutyplus.domain.task.dto.TaskResponseDto;
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
    public ResponseEntity<ResponseDto> createTask(@ApiIgnore Authentication auth, @RequestBody TaskPostDto taskPostDto){
        Long userSeq = (Long)auth.getPrincipal();
        TaskDto taskDto = taskService.createTask(userSeq, taskPostDto);
        //TODO : [통계] 로직 수정 필요
        userService.getReportContinuous(userSeq, taskDto);

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
