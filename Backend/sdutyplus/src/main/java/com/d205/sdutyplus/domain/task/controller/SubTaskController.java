package com.d205.sdutyplus.domain.task.controller;

import com.d205.sdutyplus.domain.task.dto.SubTaskRequestDto;
import com.d205.sdutyplus.domain.task.service.SubTaskService;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.d205.sdutyplus.global.response.ResponseCode.CREATE_SUBTASK_SUCCESS;
import static com.d205.sdutyplus.global.response.ResponseCode.DELETE_SUBTASK_SUCCESS;

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

    @ApiOperation(value = "서브테스크 삭제")
    @DeleteMapping("{sub_seq}")
    public ResponseEntity<?> deleteSubTask(@PathVariable(value="sub_seq") Long subTaskSeq){
        subTaskService.deleteSubTask(subTaskSeq);
        return ResponseEntity.ok().body(ResponseDto.of(DELETE_SUBTASK_SUCCESS));
    }

}
