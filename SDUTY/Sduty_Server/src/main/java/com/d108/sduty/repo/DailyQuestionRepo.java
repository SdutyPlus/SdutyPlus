package com.d108.sduty.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.DailyQuestion;

public interface DailyQuestionRepo extends JpaRepository<DailyQuestion, Integer>{

}
