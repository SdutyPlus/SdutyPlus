package com.d108.sduty.service;

import java.util.List;
import java.util.Optional;

import com.d108.sduty.dto.Profile;

public interface ProfileService {
	Profile insertProfile(Profile profile) throws Exception;
	boolean checkDupNickname(String nickname) throws Exception;
	Profile selectProfile(int seq);
	Profile selectBaseProfile(int seq);
	Profile updateProfile(Profile profile) throws Exception;
	List<Boolean> selectAllRegtime(int userSeq);
	int changeStudying(int userSeq, int flag);
	Profile selectRecommand(int userSeq);
}
