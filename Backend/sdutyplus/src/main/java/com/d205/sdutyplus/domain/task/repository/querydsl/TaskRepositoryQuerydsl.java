package com.d205.sdutyplus.domain.task.repository.querydsl;

import com.d205.sdutyplus.domain.task.dto.TaskResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskRepositoryQuerydsl {
    List<TaskResponseDto> findTaskByStartTime(Long userSeq, LocalDateTime start, LocalDateTime end);
}
