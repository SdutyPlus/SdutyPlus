package com.d205.sdutyplus.domain.task.repository;

import com.d205.sdutyplus.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
