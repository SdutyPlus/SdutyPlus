package com.d205.sdutyplus;

import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.domain.task.repository.TaskRepository;
import com.d205.sdutyplus.domain.task.repository.querydsl.TaskRepositoryQuerydsl;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.util.TimeFormatter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.d205.sdutyplus.domain.task.entity.QTask.task;
import static com.d205.sdutyplus.global.error.ErrorCode.TASK_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskRepositoryTest {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    public void findTaskBySeq() {
        //given
        Long taskSeq = 4L;
        Optional<TaskDto> taskDto = taskRepository.findTaskBySeq(taskSeq);

        //then
        assertThat(taskDto
                .orElseThrow(()->new EntityNotFoundException(TASK_NOT_FOUND)).getSeq())
                .isEqualTo(taskSeq);

    }

    @Test
    public void getReportTotalTime(){
        LocalDateTime startTime = TimeFormatter.StringToLocalDateTime("2022-11-04 00:00:00");
        LocalDateTime endTime = TimeFormatter.StringToLocalDateTime("2022-11-04 23:59:59");
        Long userSeq = 2L;

       Integer duration = queryFactory
                .select(
                        task.durationTime.sum()
                )
                .from(task)
                .where(task.startTime.between(startTime, endTime).and(task.ownerSeq.eq(userSeq)))
               .fetchFirst();
       String time = TimeFormatter.msToTime(duration);
       assertThat(time).isEqualTo("00:10:00");
    }

    @Test
    @DisplayName("시간 중복 검사")
    public void existTimeDuplicate(){
        Long userSeq = 20L;
        Long taskSeq = 106L;
        LocalDateTime startTime = TimeFormatter.StringToLocalDateTime("2022-11-15 17:13:37");
        LocalDateTime endTime = TimeFormatter.StringToLocalDateTime("2022-11-15 17:17:00");

        Boolean test = queryFactory
                .selectFrom(task)
                .where(task.seq.ne(taskSeq)
                        .and(task.ownerSeq.eq(userSeq))
                        .and(
                                task.startTime.loe(startTime).and(task.endTime.goe(startTime))
                                        .or(task.endTime.goe(endTime).and(task.startTime.loe(endTime)))
                                        .or(task.startTime.goe(startTime).and(task.endTime.loe(endTime)))
                        )
                )
                .fetchFirst() != null;

        assertThat(test).isTrue();
    }
}
