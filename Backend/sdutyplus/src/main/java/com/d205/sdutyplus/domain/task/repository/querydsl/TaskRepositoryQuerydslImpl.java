package com.d205.sdutyplus.domain.task.repository.querydsl;

import com.d205.sdutyplus.domain.task.dto.QSubTaskResponseDto;
import com.d205.sdutyplus.domain.task.dto.SubTaskResponseDto;
import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.task.dto.TaskResponseDto;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.d205.sdutyplus.domain.task.entity.QSubTask.subTask;
import static com.d205.sdutyplus.domain.task.entity.QTask.task;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryQuerydslImpl implements TaskRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TaskResponseDto> findTaskByStartTime(Long userSeq, LocalDateTime start, LocalDateTime end) {
        Map<Task, List<SubTaskResponseDto>> transform = queryFactory
                .selectFrom(task)
                .leftJoin(task.subTasks, subTask)
                .where(task.startTime.between(start, end).and(task.ownerSeq.eq(userSeq)))
                .transform(groupBy(task).as(list(new QSubTaskResponseDto(subTask.seq, subTask.content))));

        return transform.entrySet().stream()
                .map(entry -> new TaskResponseDto(entry.getKey().getSeq(), entry.getKey().getStartTime(), entry.getKey().getEndTime(), entry.getKey().getTitle(), entry.getValue()))
                .collect(Collectors.toList());

    }

    @Override
    public Optional<TaskDto> findTaskBySeq(Long taskSeq) {
        Map<Task, List<String>> transform = queryFactory
                .selectFrom(task)
                .leftJoin(task.subTasks, subTask)
                .where(task.seq.eq(taskSeq))
                .transform(groupBy(task).as(list(subTask.content)));

        return (Optional<TaskDto>) transform.entrySet().stream()
                .map(entry -> TaskDto.builder()
                        .seq(entry.getKey().getSeq())
                        .startTime(entry.getKey().getStartTime())
                        .endTime(entry.getKey().getEndTime())
                        .title(entry.getKey().getTitle())
                        .contents(entry.getValue())
                        .build())
                .findFirst();
    }

    @Override
    public Integer getReportTotalTime(Long userSeq, LocalDateTime startTime, LocalDateTime endTime) {
        return queryFactory
                .select(
                        task.durationTime.sum()
                )
                .from(task)
                .where(task.startTime.between(startTime, endTime).and(task.ownerSeq.eq(userSeq)))
                .fetchFirst();
    }

}
