package com.d108.sduty.service;

import java.util.List;

import com.d108.sduty.dto.Achievement;

public interface UserAchieveService {
	int insertUserAchieve(int userSeq, int achieveSeq);
	List<Achievement> selectUserAchieve(int userSeq);
	Achievement selectAchievement(int achievementSeq);
}
