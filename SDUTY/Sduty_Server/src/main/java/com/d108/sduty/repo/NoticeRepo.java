package com.d108.sduty.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.Notice;

public interface NoticeRepo extends JpaRepository<Notice, Integer>{
	
}
