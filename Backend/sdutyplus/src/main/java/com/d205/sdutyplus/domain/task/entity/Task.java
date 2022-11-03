package com.d205.sdutyplus.domain.task.entity;

import com.d205.sdutyplus.global.enums.TimeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "UNSIGNED INT")
    private Long seq;
    @Column(name = "owner_seq", columnDefinition = "UNSIGNED INT", nullable = false)
    private Long ownerSeq;
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    @Column(name = "duration_time", nullable = false)
    private int durationTime;
    @Column(nullable = false)
    private String content;

    @Builder
    public Task(String startTime, String endTime, int durationTime, String content){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TimeEnum.dateTimeFormat);
        LocalDateTime startLDT = LocalDateTime.parse(startTime, formatter);
        LocalDateTime endLDT = LocalDateTime.parse(endTime, formatter);

        this.startTime = startLDT;
        this.endTime = endLDT;
        this.durationTime = durationTime;
        this.content = content;
    }
}
