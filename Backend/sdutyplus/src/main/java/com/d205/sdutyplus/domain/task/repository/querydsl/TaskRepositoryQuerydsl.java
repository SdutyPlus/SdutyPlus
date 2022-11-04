package com.d205.sdutyplus.domain.task.repository.querydsl;

import com.d205.sdutyplus.domain.task.dto.TaskDto;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepositoryQuerydsl {
    List<TaskDto> findTaskByStartTime(LocalDateTime start, LocalDateTime end);
}
