package com.d108.sduty.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.d108.sduty.dto.Reply;

public interface ReplyRepo extends JpaRepository<Reply, Integer> {
	List<Reply> findAllByStorySeqOrderByRegtimeDesc(int seq);
	int countAllByStorySeq(int storySeq);
}
