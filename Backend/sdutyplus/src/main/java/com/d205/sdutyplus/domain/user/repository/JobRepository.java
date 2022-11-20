package com.d205.sdutyplus.domain.user.repository;

import com.d205.sdutyplus.global.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findBySeq(Long seq);
    Optional<Job> findByJobName(String jobName);
}
