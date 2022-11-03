package com.d205.sdutyplus.domain.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SubTask {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "UNSIGNED INT")
    private Long seq;
    @Column(name = "task_seq", columnDefinition = "UNSIGNED INT", nullable = false)
    private Long taskSeq;
    @Column(nullable = false)
    private String content;
}
