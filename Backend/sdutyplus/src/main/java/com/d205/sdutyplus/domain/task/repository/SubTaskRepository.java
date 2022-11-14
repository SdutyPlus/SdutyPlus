package com.d205.sdutyplus.domain.task.repository;

import com.d205.sdutyplus.domain.task.entity.SubTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubTaskRepository extends JpaRepository<SubTask, Long> {
    public void deleteByTaskSeq(Long taskSeq);
    public List<SubTask> findAllByTaskSeq(Long taskSeq);
}
