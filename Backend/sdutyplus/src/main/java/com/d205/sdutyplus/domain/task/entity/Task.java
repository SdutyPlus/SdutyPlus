package com.d205.sdutyplus.domain.task.entity;

import com.d205.sdutyplus.util.TimeFormatter;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seq;
    @Column(name = "owner_seq", columnDefinition = "INT UNSIGNED", nullable = false)
    private Long ownerSeq;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    @Column(name = "duration_time", nullable = false)
    private int durationTime;
    @Column(name="title", nullable = false, length=200)
    private String title;

    @OneToMany(mappedBy = "taskSeq")
    private List<SubTask> subTasks = new ArrayList<>();

    @Builder
    public Task(String startTime, String endTime, String title){
        LocalDateTime startLDT = TimeFormatter.StringToLocalDateTime(startTime);
        LocalDateTime endLDT = TimeFormatter.StringToLocalDateTime(endTime);
        this.startTime = startLDT;
        this.endTime = endLDT;
        this.durationTime = TimeFormatter.getDurationTime(startLDT, endLDT);
        this.title = title;
    }

}
