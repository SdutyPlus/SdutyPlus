package com.d205.sdutyplus.domain.statistics.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@DynamicInsert
public class DailyStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seq;

    @Column(name = "user_seq", columnDefinition = "INT UNSIGNED")
    private Long userSeq;

    @Column(name = "job_seq", columnDefinition = "INT UNSIGNED")
    private Long jobSeq;

    @Column(name = "daily_study_time")
    @ColumnDefault("0")
    private Long dailyStudyTime;

    public void plusStudyTime(Long time){
        this.dailyStudyTime += time;
    }
}
