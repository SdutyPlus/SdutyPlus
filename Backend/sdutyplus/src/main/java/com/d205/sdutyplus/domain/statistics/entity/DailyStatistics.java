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

    @Column(name = "daily_study_time")
    @ColumnDefault("0")
    private Long dailyStudyTime;

    @Column(name = "daily_study_minute")
    @ColumnDefault("0")
    private Long dailyStudyMinute;

    public void plusStudyTime(Long time, Long minute){
        this.dailyStudyTime += time;
        this.dailyStudyMinute += minute;

        if (this.dailyStudyMinute >= 60) {
            this.dailyStudyTime += 1;
            this.dailyStudyMinute -= 60;
        }
    }
}
