package com.d108.sduty.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.UserInterest;

public interface UserInterestRepo extends JpaRepository<UserInterest, Integer> {
	List<UserInterest> findAllByUserSeq(int userSeq);
}
