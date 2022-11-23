package com.d108.sduty.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class JobHashtag {
	@Id
	@Column(name = "job_hashtag_seq")
	private int seq;
	@Column(name = "job_hashtag_name")
	private String name;
}
