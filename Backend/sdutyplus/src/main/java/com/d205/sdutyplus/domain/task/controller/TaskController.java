package com.d205.sdutyplus.domain.task.controller;

import com.d205.sdutyplus.domain.task.dto.TaskRequestDto;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.domain.task.service.TaskService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @ApiOperation(value = "테스크 등록")
    @PostMapping("")
    public ResponseEntity<?> registTask(@RequestBody TaskRequestDto taskRequestDto){
        Task registedTask = taskService.registTask(new Long(1), taskRequestDto);
        if(registedTask==null){
            return ResponseEntity.badRequest()
                    .body("테스크가 등록되지 않았습니다.");
        }
        return ResponseEntity.ok().body(null);
    }
}
