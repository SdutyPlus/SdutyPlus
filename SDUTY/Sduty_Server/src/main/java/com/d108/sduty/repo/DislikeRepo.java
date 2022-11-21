package com.d108.sduty.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.d108.sduty.dto.Dislike;

public interface DislikeRepo extends JpaRepository<Dislike, Integer>{
	@Query(value="select dislike_story_seq from dislike where dislike_user_seq = ?1", nativeQuery=true)
	List<Integer> findAllDislikes(int userSeq);
}
