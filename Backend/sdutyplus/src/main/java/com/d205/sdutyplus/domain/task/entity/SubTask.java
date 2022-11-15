package com.d205.sdutyplus.domain.task.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubTask {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seq;
    @Column(name = "task_seq", columnDefinition = "INT UNSIGNED", nullable = false)
    private Long taskSeq;
    @Column(nullable = false, length=50)
    private String content;

    @Builder
    public SubTask(Long taskSeq, String content){
        this.taskSeq = taskSeq;
        this.content = content;
    }

}
