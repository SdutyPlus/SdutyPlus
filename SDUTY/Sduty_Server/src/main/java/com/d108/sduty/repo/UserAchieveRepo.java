package com.d108.sduty.repo;

import java.util.List;

import com.d108.sduty.dto.Achievement;

public interface UserAchieveRepo {
	int insertUserAchieve(int userSeq, int achievementSeq);
	List<Achievement> selectUserAchieve(int userSeq);
	Achievement selectAchievement(int achievementSeq);
}
