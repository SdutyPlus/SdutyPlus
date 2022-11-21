package com.d108.sduty.service;

import java.util.List;

import com.d108.sduty.dto.Scrap;

public interface ScrapService {
	boolean checkAlreadyScrap(int userSeq, int storySeq);
	Scrap insertScrap(Scrap scrap);	
	void deleteScrap(int userSeq, int storySeq);
	List<Integer> selectScrapSeqs(int userSeq);
}
