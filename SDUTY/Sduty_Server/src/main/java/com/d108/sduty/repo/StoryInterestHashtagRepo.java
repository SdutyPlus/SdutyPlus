package com.d108.sduty.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.StoryInterest;

public interface StoryInterestHashtagRepo extends JpaRepository<StoryInterest, Integer> {
	List<StoryInterest> findAllBySeq(int seq);
	List<StoryInterest> findAllByinterestSeq(int seq);
}
