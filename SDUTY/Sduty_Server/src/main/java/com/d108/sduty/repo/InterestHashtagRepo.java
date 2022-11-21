package com.d108.sduty.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.InterestHashtag;

public interface InterestHashtagRepo extends JpaRepository<InterestHashtag, Integer>{
	InterestHashtag findByName(String interestName);
}
