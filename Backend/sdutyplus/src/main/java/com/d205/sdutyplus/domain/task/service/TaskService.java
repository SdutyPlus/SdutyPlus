package com.d205.sdutyplus.domain.task.service;


import com.d205.sdutyplus.domain.task.dto.ReportDto;
import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.task.dto.TaskPostDto;
import com.d205.sdutyplus.domain.task.entity.SubTask;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.domain.task.exception.TimeDuplicateException;
import com.d205.sdutyplus.domain.task.repository.SubTaskRepository;
import com.d205.sdutyplus.domain.task.repository.TaskRepository;
import com.d205.sdutyplus.domain.task.repository.querydsl.TaskRepositoryQuerydsl;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.global.error.exception.InvalidInputException;
import com.d205.sdutyplus.util.TimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static com.d205.sdutyplus.global.error.ErrorCode.TASK_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TaskService{
    private final TaskRepository taskRepository;
    private final SubTaskRepository subTaskRepository;
    private final TaskRepositoryQuerydsl taskRepositoryQuerydsl;

    @Transactional
    public TaskDto createTask(Long userSeq, TaskPostDto taskPostDto){
        final Task task = taskPostDto.toEntity();
        task.setOwnerSeq(userSeq);

        timeDuplicateCheck(userSeq, 0L, task.getStartTime(), task.getEndTime());

        final Task createdTask = taskRepository.save(task);
        final List<String> createdSubTasks = createSubTask(createdTask.getSeq(), taskPostDto.getContents());

        final TaskDto taskDto = TaskDto.builder()
                .seq(createdTask.getSeq())
                .startTime(createdTask.getStartTime())
                .endTime(createdTask.getEndTime())
                .title(createdTask.getTitle())
                .contents(createdSubTasks)
                .build();
        return taskDto;
    }

    public TaskDto getTaskDetail(Long taskSeq){
        return taskRepositoryQuerydsl.findTaskBySeq(taskSeq)
                .orElseThrow(()->new EntityNotFoundException(TASK_NOT_FOUND));
    }

    @Transactional
    public void updateTask(Long taskSeq, TaskDto taskDto){
        if(!taskSeq.equals(taskDto.getSeq())){
            throw new InvalidInputException();
        }

        //task
        final Task task = getTask(taskSeq);
        final Task updatedTask = taskDto.toEntity();

        timeDuplicateCheck(task.getOwnerSeq(), taskSeq, updatedTask.getStartTime(), updatedTask.getEndTime());

        task.setStartTime(updatedTask.getStartTime());
        task.setEndTime(updatedTask.getEndTime());
        task.setDurationTime(updatedTask.getDurationTime());
        task.setTitle(updatedTask.getTitle());

        //subtask
        deleteSubTaskByTaskSeq(taskSeq);
        createSubTask(taskSeq, taskDto.getContents());
    }

    @Transactional
    public void deleteTask(Long taskSeq){
        subTaskRepository.deleteByTaskSeq(taskSeq);
        taskRepository.deleteById(taskSeq);
    }

    public ReportDto getDailyReport(Long userSeq, String date){
        final LocalDateTime startTime = TimeFormatter.StringToLocalDateTime(date+" 00:00:00");
        final LocalDateTime endTime = TimeFormatter.StringToLocalDateTime(date+" 23:59:59");
        final List<TaskDto> taskDtos = taskRepositoryQuerydsl.findTaskByStartTime(userSeq, startTime, endTime);

        final ReportDto reportResponseDto = new ReportDto(taskDtos);

        return reportResponseDto;
    }

    public String getReportTotalTime(Long userSeq, String date){
        final LocalDateTime startTime = TimeFormatter.StringToLocalDateTime(date+" 00:00:00");
        final LocalDateTime endTime = TimeFormatter.StringToLocalDateTime(date+" 23:59:59");

        Integer duration = taskRepositoryQuerydsl.getReportTotalTime(userSeq, startTime, endTime);
        if(duration == null){
            duration = 0;
        }
        return TimeFormatter.msToTime(duration);
    }

    @Transactional
    public List<String> createSubTask(Long taskSeq, List<String> subtasks){
        final List<String> result = new LinkedList<>();
        for(String subtask : subtasks){
            final SubTask subTask = SubTask.builder()
                    .taskSeq(taskSeq)
                    .content(subtask)
                    .build();
            result.add(subTaskRepository.save(subTask).getContent());
        }
        return result;
    }

    @Transactional
    public void deleteSubTaskByTaskSeq(Long taskSeq){
        subTaskRepository.deleteByTaskSeq(taskSeq);
    }

    /**
     * private
     */
    private Task getTask(Long taskSeq){
        return taskRepository.findById(taskSeq)
                .orElseThrow(()->new EntityNotFoundException(TASK_NOT_FOUND));
    }



    private void timeDuplicateCheck(Long userSeq, Long taskSeq, LocalDateTime startTime, LocalDateTime endTime){
        final int duplicatedCnt = taskRepositoryQuerydsl.getTimeDuplicatedTaskCnt(userSeq, taskSeq, startTime, endTime);
        if(duplicatedCnt > 0){
            throw new TimeDuplicateException();
        }
    }

}
