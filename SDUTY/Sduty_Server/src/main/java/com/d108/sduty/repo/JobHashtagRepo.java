package com.d108.sduty.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.JobHashtag;

public interface JobHashtagRepo extends JpaRepository<JobHashtag, Integer> {
	JobHashtag findByName(String jobName);
	List<JobHashtag> findAllByName(String jobName);
}
