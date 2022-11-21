package com.d108.sduty.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.Alarm;
import com.d108.sduty.dto.Study;

public interface AlarmRepo extends JpaRepository<Alarm, String> {
}
