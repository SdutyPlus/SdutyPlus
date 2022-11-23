package com.d108.sduty.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d108.sduty.dto.Scrap;
import com.d108.sduty.repo.ScrapRepo;

@Service
public class ScrapServiceImpl implements ScrapService {

	@Autowired
	private ScrapRepo scrapRepo;
	
	@Override
	public boolean checkAlreadyScrap(int userSeq, int storySeq) {
		return scrapRepo.existsByUserSeqAndStorySeq(userSeq,  storySeq);
	}

	@Override
	public Scrap insertScrap(Scrap scrap) {
		return scrapRepo.save(scrap);
	}

	@Override
	public void deleteScrap(int userSeq, int storySeq) {
		scrapRepo.deleteByUserSeqAndStorySeq(userSeq, storySeq);
	}

	@Override
	public List<Integer> selectScrapSeqs(int userSeq) {
		List<Integer> list = new ArrayList<Integer>();
		for(Scrap s : scrapRepo.findAllByUserSeq(userSeq)) {
			list.add(s.getStorySeq());
		}
		return list;
	}

}
