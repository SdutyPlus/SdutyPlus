package com.d108.sduty.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Qna {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="qna_seq")
	private int seq;
	@Column(name="ques_user_seq")
	private int userSeq;
	@Column(name="ques_title")
	private String title;
	@Column(name="ques_content")
	private String content;	
	@Column(name="ques_writer")
	private String writer;
	@Column(name="ans_writer")
	private String ansWriter;
	@Column(name="ans_content")
	private String answer;	
}
