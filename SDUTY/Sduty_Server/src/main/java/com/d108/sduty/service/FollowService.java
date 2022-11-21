package com.d108.sduty.service;

import java.util.List;

import com.d108.sduty.dto.Follow;

public interface FollowService {
	Follow insertFollow(Follow follow);
	boolean findFollowing(int followerSeq, int followeeSeq);
	void deleteFollow(Follow follow);
	List<Follow> selectFollower(int seq);
	List<Follow> selectFollowee(int seq);
}
