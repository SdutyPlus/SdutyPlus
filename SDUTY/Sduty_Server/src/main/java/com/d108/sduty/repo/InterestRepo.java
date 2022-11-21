package com.d108.sduty.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.InterestHashtag;

public interface InterestRepo extends JpaRepository<InterestHashtag, Integer> {	
}
