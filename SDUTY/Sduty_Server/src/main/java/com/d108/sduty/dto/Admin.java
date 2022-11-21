package com.d108.sduty.dto;

import java.util.HashSet;
import java.util.Set;

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
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="admin_seq")
	private int seq;
	@Column(name="admin_id")
	private String id;
	@Column(name="admin_password")
	private String password;
	@Column(name = "admin_name")
	private String name;
	
	@OneToMany(mappedBy = "writerSeq", fetch = FetchType.EAGER)
	private Set<Notice> notices = new HashSet<>();
		
}
