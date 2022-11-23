package com.d108.sduty.repo;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.Task;

public interface TaskRepo extends JpaRepository<Task, String>{
	Optional<Task> findBySeq(int taskSeq);
	boolean existsBySeq(int taskSeq);
	@Transactional
	void deleteBySeq(int taskSeq);
}
