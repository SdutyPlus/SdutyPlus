package com.d205.sdutyplus.domain.task.service;


import com.d205.sdutyplus.domain.task.dto.TaskRequestDto;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.domain.task.repository.TaskRepository;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static com.d205.sdutyplus.global.error.ErrorCode.TASK_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    @Transactional
    public Task createTask(Long userSeq, TaskRequestDto taskRequestDto){
        Task task = taskRequestDto.toEntity();
        task.setOwnerSeq(userSeq);
        return taskRepository.save(task);
    }

    @Transactional
    public void updateTask(Long taskSeq, TaskRequestDto taskRequestDto){
        Task task = getTask(taskSeq);
        Task updatedTask = taskRequestDto.toEntity();
        task.setStartTime(updatedTask.getStartTime());
        task.setEndTime(updatedTask.getEndTime());
        task.setDurationTime(updatedTask.getDurationTime());
        task.setContent(updatedTask.getContent());
    }

    @Transactional
    public void deleteTask(Long taskSeq){
        //TODO : Task와 연결된 subTask 삭제
        taskRepository.deleteById(taskSeq);
    }

    //get & set => private
    private Task getTask(Long taskSeq){
        return taskRepository.findById(taskSeq)
                .orElseThrow(()->new EntityNotFoundException(TASK_NOT_FOUND));
    }

}
