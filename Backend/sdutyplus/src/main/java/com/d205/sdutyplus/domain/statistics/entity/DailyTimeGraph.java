package com.d205.sdutyplus.domain.statistics.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@DynamicInsert
public class DailyTimeGraph {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seq;

    @Column(name = "start_time", columnDefinition = "INT UNSIGNED")
    private Long startTIme;

    @Column(name = "end_time", columnDefinition = "INT UNSIGNED")
    private Long endTime;
    @Column(name = "count", columnDefinition = "INT UNSIGNED")
    private Long count;

    public void counter(Long cnt){
        this.count += cnt;
    }

}
