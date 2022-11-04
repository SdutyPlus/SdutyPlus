package com.d205.sdutyplus.domain.task.service;

import com.d205.sdutyplus.domain.task.dto.SubTaskRequestDto;
import com.d205.sdutyplus.domain.task.entity.SubTask;
import com.d205.sdutyplus.domain.task.repository.SubTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class SubTaskService {
    private final SubTaskRepository subTaskRepository;
    private final TaskService taskService;

    @Transactional
    public void createSubTask(SubTaskRequestDto subTaskRequestDto){
        SubTask subTask = subTaskRequestDto.toEntity();
        subTaskRepository.save(subTask);
        taskService.addSubTask(subTask);
    }
}
