package com.d108.sduty.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.JobHashtag;

public interface JobRepo extends JpaRepository<JobHashtag, Integer> {

}
