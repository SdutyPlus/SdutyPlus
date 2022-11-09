package com.d205.sdutyplus.domain.task.repository.querydsl;

import com.d205.sdutyplus.domain.task.dto.TaskResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepositoryQuerydsl {
    List<TaskResponseDto> findTaskByStartTime(Long userSeq, LocalDateTime start, LocalDateTime end);
    Optional<TaskResponseDto> findTaskBySeq(Long taskSeq);
    public Integer getReportTotalTime(Long userSeq, LocalDateTime startTime, LocalDateTime endTime);
}
