package com.d205.sdutyplus;

import com.d205.sdutyplus.domain.task.dto.QSubTaskResponseDto;
import com.d205.sdutyplus.domain.task.dto.SubTaskResponseDto;
import com.d205.sdutyplus.domain.task.dto.TaskResponseDto;
import com.d205.sdutyplus.domain.task.entity.Task;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.util.TimeFormatter;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.d205.sdutyplus.domain.task.entity.QSubTask.subTask;
import static com.d205.sdutyplus.domain.task.entity.QTask.task;
import static com.d205.sdutyplus.global.error.ErrorCode.TASK_NOT_FOUND;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskRepositoryTest {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Test
    public void findTaskBySeq() {
        //given
        Long taskSeq = 4L;
        Map<Task, List<SubTaskResponseDto>> transform = queryFactory
                .selectFrom(task)
                .leftJoin(task.subTasks, subTask)
                .where(task.seq.eq(taskSeq))
                .transform(groupBy(task).as(list(new QSubTaskResponseDto(subTask.seq, subTask.content))));


        Optional<TaskResponseDto> taskResponseDto = (Optional<TaskResponseDto>) transform.entrySet().stream()
                .map(entry -> new TaskResponseDto(entry.getKey().getSeq(), entry.getKey().getStartTime(), entry.getKey().getEndTime(), entry.getKey().getContent(), entry.getValue()))
                .findFirst();

        //then
        assertThat(taskResponseDto
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
}
