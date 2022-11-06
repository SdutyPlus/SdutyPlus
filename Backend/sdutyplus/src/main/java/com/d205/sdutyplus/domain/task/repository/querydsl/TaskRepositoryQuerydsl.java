package com.d205.sdutyplus.domain.task.repository.querydsl;

import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.task.dto.TaskResponseDto;
import com.d205.sdutyplus.domain.task.entity.Task;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepositoryQuerydsl {
    List<TaskResponseDto> findTaskByStartTime(LocalDateTime start, LocalDateTime end);
}
