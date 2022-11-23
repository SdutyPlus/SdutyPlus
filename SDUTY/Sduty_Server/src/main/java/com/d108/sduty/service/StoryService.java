package com.d108.sduty.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.d108.sduty.dto.PagingResult;
import com.d108.sduty.dto.Reply;
import com.d108.sduty.dto.Story;

public interface StoryService {
	Story insertStory(Story story);
	Story updateStory(Story story);	
	Story findById(int storySeq);
	List<Story> findAll();
	Page<Story> findAllByWriterSeqInOrderByRegtimeDesc(List<Integer> writerSeqs, Pageable pageable);
	PagingResult<Story> selectStoryInSeq(List<Integer> storySeqs, Pageable pageable);
	void deleteStory(int storySeq);
	
	List<Reply> selectReplyByStorySeq(int storySeq);
	Reply insertReply(Reply reply);
	Reply updateReply(Reply reply);
	Reply deleteReply(int replySeq);
	
	void doDislike(int userSeq, int storySeq);
	PagingResult<Story> findBywriterSeq(int writerSeq, int userSeq, Pageable pageable);
}
