package com.d108.sduty.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.d108.sduty.dto.InterestHashtag;
import com.d108.sduty.dto.JobHashtag;
import com.d108.sduty.dto.PagingResult;
import com.d108.sduty.dto.Profile;
import com.d108.sduty.dto.Reply;
import com.d108.sduty.dto.Story;
import com.d108.sduty.dto.Timeline;

public interface TimelineService {
	public PagingResult<Timeline> selectAllByUserSeqsOrderByRegtime(int userSeq, List<Integer> writerSeq, Pageable pageable);
	public PagingResult<Timeline> selectAllByUserSeqsWithTag(int userSeq, List<Integer> writerSeq, String jobName, Pageable pageable);
	public PagingResult<Timeline> selectAllByJobName(int userSeq, String jobName, Pageable pageable);
	public PagingResult<Timeline> selectAllByInterestName(int userSeq, String interestName, Pageable pageable);
	public Timeline selectDetailTimeline(int storySeq, int userSeq);
	public PagingResult<Timeline> selectAllTimelines(int userSeq, Pageable pageable);
	public Timeline selectRecommandTimeline(int userSeq);
	public Profile getProfile(int userSeq);
	public void setInterestList(Timeline t, Story s);
	public List<Reply> findAllReplyByStorySeqOrderByRegtimeDesc(int storySeq);
	public JobHashtag getJobHashtag(String jobName);
	public InterestHashtag getInterestHashtag(String interestName);
	
	PagingResult<Timeline> selectAllPagingTimelines(Pageable pageable, int userSeq);
}
