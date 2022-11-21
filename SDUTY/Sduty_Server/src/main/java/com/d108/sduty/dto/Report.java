package com.d108.sduty.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Report {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="report_seq")
	private int seq;
	@Column(name="report_owner_seq")
	private int ownerSeq;
	@Column(name="report_date")
	private String date;
	
	@OneToMany(mappedBy="reportSeq", fetch = FetchType.LAZY)
	private List<Task> task = new ArrayList<Task>();
}
