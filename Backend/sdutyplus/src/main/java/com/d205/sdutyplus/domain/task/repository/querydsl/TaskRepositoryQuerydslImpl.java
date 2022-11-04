package com.d205.sdutyplus.domain.task.repository.querydsl;

import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class TaskRepositoryQuerydslImpl implements TaskRepositoryQuerydsl{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<TaskDto> findTaskByStartTime(LocalDateTime start, LocalDateTime end) {
//        final List<TaskDto> tasks = queryFactory
//                .select(new TaskDto(
//                ))
        return null;
    }
}
