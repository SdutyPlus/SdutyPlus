package com.d205.sdutyplus.domain.task.repository.querydsl;

import com.d205.sdutyplus.domain.task.dto.QSubTaskResponseDto;
import com.d205.sdutyplus.domain.task.dto.SubTaskResponseDto;
import com.d205.sdutyplus.domain.task.dto.TaskResponseDto;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
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
                .map(entry -> new TaskResponseDto(entry.getKey().getSeq(), entry.getKey().getStartTime(), entry.getKey().getEndTime(), entry.getKey().getContent(), entry.getValue()))
                .collect(Collectors.toList());

    }

    @Override
    public Optional<TaskResponseDto> findTaskBySeq(Long taskSeq) {
        Map<Task, List<SubTaskResponseDto>> transform = queryFactory
                .selectFrom(task)
                .leftJoin(task.subTasks, subTask)
                .where(task.seq.eq(taskSeq))
                .transform(groupBy(task).as(list(new QSubTaskResponseDto(subTask.seq, subTask.content))));

//        System.out.println(transform.entrySet().stream()
//                .map(entry -> new TaskResponseDto(entry.getKey().getSeq(), entry.getKey().getStartTime(), entry.getKey().getEndTime(), entry.getKey().getContent(), entry.getValue()))
//                .findFirst()
//        );
        return (Optional<TaskResponseDto>) transform.entrySet().stream()
                .map(entry -> new TaskResponseDto(entry.getKey().getSeq(), entry.getKey().getStartTime(), entry.getKey().getEndTime(), entry.getKey().getContent(), entry.getValue()))
                .findFirst();

    }

}
