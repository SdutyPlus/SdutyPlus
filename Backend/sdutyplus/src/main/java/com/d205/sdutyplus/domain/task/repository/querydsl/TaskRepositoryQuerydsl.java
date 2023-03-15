package com.d205.sdutyplus.domain.task.repository.querydsl;

import com.d205.sdutyplus.domain.task.dto.TaskDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TaskRepositoryQuerydsl {
    List<TaskDto> findTaskByStartTime(Long userSeq, LocalDateTime start, LocalDateTime end);
    Optional<TaskDto> findTaskBySeq(Long taskSeq);
    Integer getReportTotalTime(Long userSeq, LocalDateTime startTime, LocalDateTime endTime);
    boolean getTimeDuplicatedTaskCnt (Long userSeq, Long taskSeq, LocalDateTime startTime, LocalDateTime endTime);
    List<String> getReportDateByOwnerSeq (Long userSeq);
}
