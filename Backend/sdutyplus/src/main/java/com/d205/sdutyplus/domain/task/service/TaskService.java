package com.d205.sdutyplus.domain.task.service;


import com.d205.sdutyplus.domain.task.dto.ReportResponseDto;
import com.d205.sdutyplus.domain.task.dto.SubTaskResponseDto;
import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.task.dto.TaskResponseDto;
import com.d205.sdutyplus.domain.task.entity.SubTask;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.domain.task.repository.SubTaskRepository;
import com.d205.sdutyplus.domain.task.repository.TaskRepository;
import com.d205.sdutyplus.domain.task.repository.querydsl.TaskRepositoryQuerydsl;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.util.TimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.d205.sdutyplus.global.error.ErrorCode.TASK_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TaskService{
    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    private final TaskRepositoryQuerydsl taskRepositoryQuerydsl;

    @Transactional
    public Task createTask(Long userSeq, TaskDto taskRequestDto){
        Task task = taskRequestDto.toEntity();
        task.setOwnerSeq(userSeq);
        return taskRepository.save(task);
    }

    public ReportResponseDto getTaskByDate(String date){
        LocalDateTime startTime = TimeFormatter.StringToLocalDateTime(date+" 00:00:00");
        LocalDateTime endTime = TimeFormatter.StringToLocalDateTime(date+" 23:59:59");
//        List<Task> tasks = taskRepository.findAllByStartTimeBetween(startTime, endTime);
        List<TaskResponseDto> taskResponseDtos = taskRepositoryQuerydsl.findTaskByStartTime(startTime, endTime);

        //TODO: QueryDSL로 변경해야 함
//        List<TaskResponseDto> taskResponseDtos = new ArrayList<>();
//        for(Task task : tasks){
//            List<SubTask> subTasks = subTaskRepository.findAllByTaskSeq(task.getSeq());
//            List<SubTaskResponseDto> subTaskResponseDtos = new ArrayList<>();
//            for(SubTask subTask : subTasks){
//                subTaskResponseDtos.add(new SubTaskResponseDto(subTask.getSeq(), subTask.getContent()));
//            }
//            taskResponseDtos.add(new TaskResponseDto(task.getSeq(), TimeFormatter.LocalDateTimeToString(task.getStartTime()), TimeFormatter.LocalDateTimeToString(task.getEndTime()), task.getContent(), subTaskResponseDtos));
//        }
        //
        ReportResponseDto reportResponseDto = new ReportResponseDto("00:00:00", taskResponseDtos);

        return reportResponseDto;
    }

    @Transactional
    public void updateTask(Long taskSeq, TaskDto taskRequestDto){
        Task task = getTask(taskSeq);
        Task updatedTask = taskRequestDto.toEntity();
        task.setStartTime(updatedTask.getStartTime());
        task.setEndTime(updatedTask.getEndTime());
        task.setDurationTime(updatedTask.getDurationTime());
        task.setContent(updatedTask.getContent());
    }

    @Transactional
    public void deleteTask(Long taskSeq){
        subTaskRepository.deleteByTaskSeq(taskSeq);
        taskRepository.deleteById(taskSeq);
    }

    //get & set => private
    private Task getTask(Long taskSeq){
        return taskRepository.findById(taskSeq)
                .orElseThrow(()->new EntityNotFoundException(TASK_NOT_FOUND));
    }

}
