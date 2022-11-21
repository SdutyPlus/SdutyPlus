package com.d108.sduty.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d108.sduty.dto.Achievement;
import com.d108.sduty.repo.UserAchieveRepo;

@Service
public class UserAchieveServiceImpl implements UserAchieveService{

	@Autowired
	private UserAchieveRepo userAchieveRepo;
	
	@Override
	public int insertUserAchieve(int userSeq, int achievementSeq) {
		return userAchieveRepo.insertUserAchieve(userSeq, achievementSeq);
	}

	@Override
	public List<Achievement> selectUserAchieve(int userSeq) {
		return userAchieveRepo.selectUserAchieve(userSeq);
	}

	@Override
	public Achievement selectAchievement(int achievementSeq) {
		return userAchieveRepo.selectAchievement(achievementSeq);
	}
	
	
}
