package com.d108.sduty.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Task: report's task 정보", description = "리포트의 task 상세정보")
@Entity
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="task_seq")
	private int seq;
	@Column(name="task_report_seq")
	private int reportSeq;
	@Column(name="task_title")
	private String title;
	@Column(name="task_content1")
	private String content1;
	@Column(name="task_content2")
	private String content2;
	@Column(name="task_content3")
	private String content3;
	@Column(name="task_start_time")
	private String startTime;
	@Column(name="task_end_time")
	private String endTime;
	@Column(name="task_duration_time")
	private int durationTime;
	
}
