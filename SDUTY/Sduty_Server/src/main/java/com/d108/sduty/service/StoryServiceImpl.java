package com.d108.sduty.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.d108.sduty.dto.Dislike;
import com.d108.sduty.dto.PagingResult;
import com.d108.sduty.dto.Reply;
import com.d108.sduty.dto.Story;
import com.d108.sduty.dto.StoryInterest;
import com.d108.sduty.dto.Timeline;
import com.d108.sduty.repo.DislikeRepo;
import com.d108.sduty.repo.ProfileRepo;
import com.d108.sduty.repo.ReplyRepo;
import com.d108.sduty.repo.StoryInterestHashtagRepo;
import com.d108.sduty.repo.StoryRepo;

@Service
public class StoryServiceImpl implements StoryService {

	@Autowired
	private StoryRepo storyRepo;
	
	@Autowired
	private ReplyRepo replyRepo;
	
	@Autowired
	private StoryInterestHashtagRepo storyInterestHashtagRepo;
	
	@Autowired
	private ProfileRepo profileRepo;
	
	@Autowired
	private DislikeRepo dislikeRepo;
	
	@Override
	public Story insertStory(Story story) {
		Story s = storyRepo.save(story);
		int storySeq = storyRepo.findTopByWriterSeqOrderByRegtimeDesc(s.getWriterSeq()).getSeq();
		if(story.getInterestHashtag() != null && !story.getInterestHashtag().isEmpty()) {
			for(int i : story.getInterestHashtag())
				storyInterestHashtagRepo.save(new StoryInterest(storySeq, i));
		}
		return s;
	}	
	
	@Override
	public Story updateStory(Story story) {
		int storySeq = story.getSeq();
		List<StoryInterest> listSI = storyInterestHashtagRepo.findAllBySeq(storySeq);
		if(!listSI.isEmpty()) {
			for(StoryInterest si : listSI) {
				storyInterestHashtagRepo.delete(si);
			}
		}
		if(story.getInterestHashtag() != null && !story.getInterestHashtag().isEmpty()) {
			for(int i : story.getInterestHashtag())
				storyInterestHashtagRepo.save(new StoryInterest(storySeq, i));
		}
		return storyRepo.save(story);
	}

	@Override
	public PagingResult<Story> findBywriterSeq(int writerSeq, int userSeq, Pageable pageable) {
		Page<Story> storyPage = storyRepo.findBywriterSeqOrderByRegtimeDesc(writerSeq, pageable);
		List<Story> storyList = new ArrayList<Story>();
		List<Integer> dislikes = dislikeRepo.findAllDislikes(userSeq);
		for(Story s : storyPage) {
			if(dislikes.contains(s.getSeq())) {
				continue;
			}
			storyList.add(s);
		}
		PagingResult result = new PagingResult<Story>(pageable.getPageNumber(), storyPage.getTotalPages() -1, storyList);
		return result;
	}

	@Override
	public Story findById(int storySeq) {
		Story story = null;
		if(storyRepo.findById(storySeq).isPresent()) {
			 story = storyRepo.findById(storySeq).get();
		}
		List<StoryInterest> listSI = storyInterestHashtagRepo.findAllBySeq(storySeq);
		List<Integer> ilistSI = new ArrayList<Integer>();
		for(StoryInterest si : listSI) {
			ilistSI.add(si.getInterestSeq());
		}
		story.setInterestHashtag(ilistSI);
		return story;
	}

	@Override
	public List<Story> findAll() {
		return storyRepo.findAll();
	}
	
	@Override
	public Page<Story> findAllByWriterSeqInOrderByRegtimeDesc(List<Integer> writerSeqs, Pageable pageable) {
		return storyRepo.findAllByWriterSeqInOrderByRegtimeDesc(writerSeqs, pageable);
	}

	@Override
	public void deleteStory(int storySeq) {
		storyRepo.deleteById(storySeq);
	}

	private List<Story> optConverter(List<Optional<Story>> list){
		List<Story> sList = new ArrayList<>();
		for(Optional<Story> l : list) {
			if(l.isPresent())
				sList.add(l.get());
		}
		return sList;
	}

	@Override
	public PagingResult<Story> selectStoryInSeq(List<Integer> storySeqs, Pageable pageable) {
		Page<Story> storyPage = storyRepo.findAllBySeqInOrderByRegtimeDesc(storySeqs, pageable);
		

		PagingResult result = new PagingResult<Story>(pageable.getPageNumber(), storyPage.getTotalPages(), storyPage.toList());
		return result;
	}

	@Override
	public Reply insertReply(Reply reply) {
		return replyRepo.save(reply);
	}

	@Override
	public Reply updateReply(Reply reply) {
		return replyRepo.save(reply);
	}

	@Override
	public Reply deleteReply(int replySeq) {
		Reply reply = replyRepo.findById(replySeq).get();
		replyRepo.deleteById(replySeq);
		return reply;
	}

	@Override
	public List<Reply> selectReplyByStorySeq(int storySeq) {
		List<Reply> list = replyRepo.findAllByStorySeqOrderByRegtimeDesc(storySeq);
		for(Reply item: list) {
			item.setProfile(profileRepo.findById(item.getUserSeq()).get());
		}
		return list;
	}

	@Override
	public void doDislike(int userSeq, int storySeq) {
		dislikeRepo.save(new Dislike(userSeq, storySeq));
	}
}
