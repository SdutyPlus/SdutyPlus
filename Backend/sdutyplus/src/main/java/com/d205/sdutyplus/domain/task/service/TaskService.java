package com.d205.sdutyplus.domain.task.service;


import com.d205.sdutyplus.domain.task.dto.ReportDto;
import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.task.dto.TaskPostDto;
import com.d205.sdutyplus.domain.task.entity.SubTask;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.domain.task.exception.InvalidEndTimeException;
import com.d205.sdutyplus.domain.task.exception.SubTaskCntLimitException;
import com.d205.sdutyplus.domain.task.exception.TimeDuplicateException;
import com.d205.sdutyplus.domain.task.exception.TimeReveredException;
import com.d205.sdutyplus.domain.task.repository.SubTaskRepository;
import com.d205.sdutyplus.domain.task.repository.TaskRepository;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.global.error.exception.InvalidInputException;
import com.d205.sdutyplus.util.AuthUtils;
import com.d205.sdutyplus.util.TimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.d205.sdutyplus.global.error.ErrorCode.TASK_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskService{
    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    private final AuthUtils authUtils;

    @Transactional
    public TaskDto createTask(TaskPostDto taskPostDto){
        final Long userSeq = authUtils.getLoginUserSeq();

        final Task task = taskPostDto.toEntity();
        task.setOwnerSeq(userSeq);

        timeValidCheck(userSeq, 0L, task.getStartTime(), task.getEndTime());

        final Task createdTask = taskRepository.save(task);
        createSubTask(createdTask, taskPostDto.getContents());

        final TaskDto taskDto = new TaskDto(createdTask);
        return taskDto;
    }

    public TaskDto getTaskDetail(Long taskSeq){
        final Task task = taskRepository.findById(taskSeq)
                .orElseThrow(()->new EntityNotFoundException(TASK_NOT_FOUND));
        return new TaskDto(task);
    }

    @Transactional
    public void updateTask(Long taskSeq, TaskDto taskDto){
        if(!taskSeq.equals(taskDto.getSeq())){
            throw new InvalidInputException();
        }

        //task
        final Task task = getTask(taskSeq);
        final Task updatedTask = taskDto.toEntity();

        timeValidCheck(task.getOwnerSeq(), taskSeq, updatedTask.getStartTime(), updatedTask.getEndTime());

        task.setStartTime(updatedTask.getStartTime());
        task.setEndTime(updatedTask.getEndTime());
        task.setDurationTime(updatedTask.getDurationTime());
        task.setTitle(updatedTask.getTitle());

        //subtask
        deleteSubTaskByTaskSeq(taskSeq);
        createSubTask(task, taskDto.getContents());
    }

    @Transactional
    public void deleteTask(Long taskSeq){
        taskRepository.deleteById(taskSeq);
    }

    public ReportDto getDailyReport(String date){
        final Long userSeq = authUtils.getLoginUserSeq();

        final LocalDateTime startTime = TimeFormatter.StringToLocalDateTime(date+" 00:00:00");
        final LocalDateTime endTime = TimeFormatter.StringToLocalDateTime(date+" 23:59:59");
        final List<Task> tasks = taskRepository.findTaskByStartTime(userSeq, startTime, endTime);
        final List<TaskDto> taskDtos = tasks.stream().map(TaskDto::new).collect(Collectors.toList());

        final ReportDto reportResponseDto = new ReportDto(taskDtos);

        return reportResponseDto;
    }

    public String getReportTotalTime(String date){
        final Long userSeq = authUtils.getLoginUserSeq();

        final LocalDateTime startTime = TimeFormatter.StringToLocalDateTime(date+" 00:00:00");
        final LocalDateTime endTime = TimeFormatter.StringToLocalDateTime(date+" 23:59:59");

        Integer duration = taskRepository.getReportTotalTime(userSeq, startTime, endTime);
        if(duration == null){
            duration = 0;
        }
        return TimeFormatter.msToTime(duration);
    }

    @Transactional
    public void createSubTask(Task task, List<String> subTaskContents){
        if(subTaskContents.size()>3){
            throw new SubTaskCntLimitException();
        }

        for(String subtask : subTaskContents){
            final SubTask subTask = SubTask.builder()
                    .taskSeq(task.getSeq())
                    .content(subtask)
                    .build();
            task.getSubTasks().add(subTask);
        }
    }

    @Transactional
    public void deleteSubTaskByTaskSeq(Long taskSeq){
        subTaskRepository.deleteByTaskSeq(taskSeq);
    }

    public List<String> getReportDateByOwnerSeq(){
        final Long userSeq = authUtils.getLoginUserSeq();
        return taskRepository.getReportDateByOwnerSeq(userSeq);
    }

    /**
     * private
     */
    private Task getTask(Long taskSeq){
        return taskRepository.findById(taskSeq)
                .orElseThrow(()->new EntityNotFoundException(TASK_NOT_FOUND));
    }

    private void timeValidCheck(Long userSeq, Long taskSeq, LocalDateTime startTime, LocalDateTime endTime){
        timeReversedCheck(startTime, endTime);
        timeDuplicateCheck(userSeq, taskSeq, startTime, endTime);
        endTimeValidCheck(endTime);
    }

    private void timeReversedCheck(LocalDateTime startTime, LocalDateTime endTime){
        if(!startTime.isBefore(endTime)){
            throw new TimeReveredException();
        }
    }

    private void timeDuplicateCheck(Long userSeq, Long taskSeq, LocalDateTime startTime, LocalDateTime endTime){
        final boolean duplicatedCnt = taskRepository.getTimeDuplicatedTaskCnt(userSeq, taskSeq, startTime, endTime);
        if(duplicatedCnt){
            throw new TimeDuplicateException();
        }
    }

    private void endTimeValidCheck(LocalDateTime endTime){
        LocalDateTime nowTime = TimeFormatter.getTodayDateTime();
        if(endTime.isAfter(nowTime)){
            throw new InvalidEndTimeException();
        }

    }

}
