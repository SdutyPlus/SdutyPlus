package com.d108.sduty.repo;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.d108.sduty.dto.InterestHashtag;
import com.d108.sduty.dto.Study;

public interface StudyRepo extends JpaRepository<Study, Integer>, JpaSpecificationExecutor<Study> {
	public Optional<Study> findByNameEquals(String name);
	public boolean existsByName(String name);
	public Optional<Study> findBySeq(int studySeq);
	@Transactional
	public int deleteBySeq(int studySeq);
}
