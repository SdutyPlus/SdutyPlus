package com.d108.sduty.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.Scrap;

public interface ScrapRepo extends JpaRepository<Scrap, Integer>{
	boolean existsByUserSeqAndStorySeq(int userSeq, int storySeq);
	@Transactional
	void deleteByUserSeqAndStorySeq(int userSeq, int storySeq);
	Scrap findByUserSeqAndStorySeq(int userSeq, int storySeq);
	List<Scrap> findAllByUserSeq(int userSeq);
}
