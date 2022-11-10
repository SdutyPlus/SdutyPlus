package com.d205.sdutyplus.global.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seq;

    @Column(name = "job_name", nullable = false, length = 20)
    private String jobName;
}
