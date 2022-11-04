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

    @Transactional
    public void createSubTask(SubTaskRequestDto subTaskRequestDto){
        SubTask subTask = subTaskRequestDto.toEntity();
        subTaskRepository.save(subTask);
    }

    @Transactional
    public void deleteSubTask(Long subTaskSeq){
        subTaskRepository.deleteById(subTaskSeq);
    }

    @Transactional
    public void deleteSubTaskByTask(Long TaskSeq) {
        subTaskRepository.deleteByTaskSeq(TaskSeq);
    }

    //get & set => private
}
