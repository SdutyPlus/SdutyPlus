package com.d205.sdutyplus.domain.task.repository;

import com.d205.sdutyplus.domain.task.entity.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
}
