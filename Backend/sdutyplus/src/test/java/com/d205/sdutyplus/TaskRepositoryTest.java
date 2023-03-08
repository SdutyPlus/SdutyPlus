package com.d205.sdutyplus;

import com.d205.sdutyplus.domain.task.dto.TaskDto;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.domain.task.repository.TaskRepository;
import com.d205.sdutyplus.domain.task.repository.querydsl.TaskRepositoryQuerydsl;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.util.TimeFormatter;
import com.google.type.DateTime;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.DateTemplate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
        Long userSeq = 3L;

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

    @Test
    @DisplayName("리포트 등록한 날짜 조회")
    public void getReportDateTime(){
        Long userSeq = 1L;

//        DateTemplate formattedDate2 = Expressions.dateTemplate(
//                String.class,
//                "DATE_FORMAT({0}, {1})" //printf처럼 뒤에 인자를 넣는 거 같음
//                ,task.startTime
//                ,ConstantImpl.create("%Y-%m-%d")
//        );

        StringTemplate formattedDate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})" //printf처럼 뒤에 인자를 넣는 거 같음
                ,task.startTime
                ,ConstantImpl.create("%Y-%m-%d")
        );

        List<String> reportDates = queryFactory
                .select(
                        formattedDate
                )
                .from(task)
                .where(task.ownerSeq.eq(userSeq))
                .distinct()
                .fetch();

        for(String date: reportDates){
            System.out.println(date);
        }
    }

}
