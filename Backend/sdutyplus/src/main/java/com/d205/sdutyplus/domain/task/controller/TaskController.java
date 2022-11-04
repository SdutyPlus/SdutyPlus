package com.d205.sdutyplus.domain.task.controller;

import com.d205.sdutyplus.domain.task.dto.TaskRequestDto;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.domain.task.service.TaskService;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.d205.sdutyplus.global.response.ResponseCode.CREATE_TASK_SUCCESS;
import static com.d205.sdutyplus.global.response.ResponseCode.UPDATE_TASK_SUCCESS;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @ApiOperation(value = "테스크 등록")
    @PostMapping("")
    public ResponseEntity<?> createTask(@RequestBody TaskRequestDto taskRequestDto){
        Task registedTask = taskService.createTask(new Long(1), taskRequestDto);
        return ResponseEntity.ok().body(ResponseDto.of(CREATE_TASK_SUCCESS, registedTask));
    }

    @ApiOperation(value = "테스크 수정")
    @PutMapping("/{taskSeq}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskSeq, @RequestBody TaskRequestDto taskRequestDto){
        taskService.updateTask(taskSeq, taskRequestDto);
        return ResponseEntity.ok().body(ResponseDto.of(UPDATE_TASK_SUCCESS));
    }
}
