package com.d205.sdutyplus.domain.task.repository.querydsl;

import com.d205.sdutyplus.domain.task.dto.TaskDto;
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
    public List<TaskDto> findTaskByStartTime(Long userSeq, LocalDateTime start, LocalDateTime end) {
        Map<Task, List<String>> transform = queryFactory
                .selectFrom(task)
                .leftJoin(task.subTasks, subTask)
                .where(task.startTime.between(start, end).and(task.ownerSeq.eq(userSeq)))
                .orderBy(task.startTime.asc())
                .transform(groupBy(task).as(list(subTask.content)));

        return transform.entrySet().stream()
                .map(entry -> TaskDto.builder()
                        .seq(entry.getKey().getSeq())
                        .startTime(entry.getKey().getStartTime())
                        .endTime(entry.getKey().getEndTime())
                        .title(entry.getKey().getTitle())
                        .contents(entry.getValue())
                        .build())
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

    @Override
    public int getTimeDuplicatedTaskCnt (Long userSeq, Long taskSeq, LocalDateTime startTime, LocalDateTime endTime) {
        return queryFactory
                .selectFrom(task)
                .where(task.seq.ne(taskSeq)
                        .and(task.ownerSeq.eq(userSeq))
                        .and(
                                task.startTime.loe(startTime).and(task.endTime.goe(startTime))
                                        .or(task.endTime.goe(endTime).and(task.startTime.loe(endTime))))
                )
                .fetch().size();
    }


}
