package com.d205.sdutyplus.domain.task.controller;

import com.d205.sdutyplus.domain.task.dto.SubTaskRequestDto;
import com.d205.sdutyplus.domain.task.service.SubTaskService;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.d205.sdutyplus.global.response.ResponseCode.CREATE_SUBTASK_SUCCESS;

@RestController
@RequestMapping("/task/sub")
@RequiredArgsConstructor
public class SubTaskController {

    private final SubTaskService subTaskService;

    @ApiOperation(value = "서브테스크 등록")
    @PostMapping("")
    public ResponseEntity<?> createSubTask(@RequestBody SubTaskRequestDto subTaskRequestDto){
        subTaskService.createSubTask(subTaskRequestDto);
        return ResponseEntity.ok().body(ResponseDto.of(CREATE_SUBTASK_SUCCESS));
    }
}
