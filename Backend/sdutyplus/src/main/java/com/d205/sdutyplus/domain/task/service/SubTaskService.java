package com.d205.sdutyplus.domain.task.service;

import com.d205.sdutyplus.domain.task.dto.SubTaskPostDto;
import com.d205.sdutyplus.domain.task.dto.SubTaskRequestDto;
import com.d205.sdutyplus.domain.task.entity.SubTask;
import com.d205.sdutyplus.domain.task.repository.SubTaskRepository;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.global.error.exception.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.d205.sdutyplus.global.error.ErrorCode.SUBTASK_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SubTaskService {
    private final SubTaskRepository subTaskRepository;

    @Transactional
    public void createSubTask(SubTaskPostDto subTaskRequestDto){
        SubTask subTask = subTaskRequestDto.toEntity();
        subTaskRepository.save(subTask);
    }

    @Transactional
    public void updateSubTask(Long subTaskSeq, SubTaskRequestDto subTaskRequestDto){
        SubTask subTask = getSubTask(subTaskSeq);
        if(subTaskSeq != subTaskRequestDto.getSeq()){
            throw new InvalidInputException();
        }
        subTask.setContent(subTaskRequestDto.getContent());
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
    private SubTask getSubTask(Long subTaskSeq){
        return subTaskRepository.findById(subTaskSeq)
                .orElseThrow(()-> new EntityNotFoundException(SUBTASK_NOT_FOUND));
    }
}
