package com.d108.sduty.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.Report;
import com.d108.sduty.dto.User;

public interface ReportRepo extends JpaRepository<Report, String>{
	@EntityGraph(attributePaths= {"task"})
	Report findByDateAndOwnerSeq(String date, int ownerSeq);
	User findBySeq(int userSeq);
}
