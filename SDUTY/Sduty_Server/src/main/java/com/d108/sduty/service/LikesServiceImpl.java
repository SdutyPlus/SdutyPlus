package com.d108.sduty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d108.sduty.dto.Likes;
import com.d108.sduty.repo.LikesRepo;

@Service
public class LikesServiceImpl implements LikesService {

	@Autowired
	private LikesRepo likeRepo;
	
	@Override
	public Likes insertLike(Likes like) {
		return likeRepo.save(like);
	}

	@Override
	public void deleteLike(Likes like) {
		likeRepo.delete(like);
	}

	@Override
	public boolean checkAlreadyLike(Likes like) {
		return likeRepo.existsByUserSeqAndStorySeq(like.getUserSeq(), like.getStorySeq());
	}
}
