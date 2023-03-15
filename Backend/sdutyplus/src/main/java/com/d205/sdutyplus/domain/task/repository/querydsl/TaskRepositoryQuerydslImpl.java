package com.d205.sdutyplus.domain.task.repository.querydsl;

import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.d205.sdutyplus.domain.task.entity.QSubTask.subTask;
import static com.d205.sdutyplus.domain.task.entity.QTask.task;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

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
                .map(entry -> new TaskDto(entry.getKey(), entry.getValue()))
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
                .map(entry -> new TaskDto(entry.getKey(), entry.getValue()))
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
    public boolean getTimeDuplicatedTaskCnt (Long userSeq, Long taskSeq, LocalDateTime startTime, LocalDateTime endTime) {
        return queryFactory
                .select(task.seq)
                .from(task)
                .where(task.seq.ne(taskSeq)
                        .and(task.ownerSeq.eq(userSeq))
                        .and(
                                task.startTime.loe(startTime).and(task.endTime.goe(startTime))
                                        .or(task.startTime.loe(endTime).and(task.endTime.goe(endTime)))
                                        .or(task.startTime.loe(startTime).and(task.endTime.goe(endTime)))
                        )
                )
                .fetchFirst() != null;
    }

    @Override
    public List<String> getReportDateByOwnerSeq(Long userSeq) {
        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})"
                ,task.startTime
                , ConstantImpl.create("%Y-%m-%d")
        );

        return queryFactory
                .select(
                        formattedDate
                )
                .from(task)
                .where(task.ownerSeq.eq(userSeq))
                .distinct()
                .fetch();
    }

}
