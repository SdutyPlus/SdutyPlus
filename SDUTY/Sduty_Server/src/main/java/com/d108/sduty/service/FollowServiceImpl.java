package com.d108.sduty.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d108.sduty.dto.Follow;
import com.d108.sduty.dto.InterestHashtag;
import com.d108.sduty.dto.Profile;
import com.d108.sduty.dto.UserInterest;
import com.d108.sduty.repo.FollowRepo;
import com.d108.sduty.repo.InterestHashtagRepo;
import com.d108.sduty.repo.ProfileRepo;
import com.d108.sduty.repo.UserInterestRepo;

@Service
public class FollowServiceImpl implements FollowService {

	@Autowired
	private FollowRepo followRepo;
	
	@Autowired 
	private ProfileRepo profileRepo;

	@Autowired
	private UserInterestRepo userInterestRepo;
	
	@Autowired
	private InterestHashtagRepo interestHashtag;
	
	@Override
	public List<Follow> selectFollower(int seq) {
		List<Optional<Follow>> followers = followRepo.findByFollowerSeq(seq);
		List<Follow> followerList = new ArrayList<>();
		for(Optional<Follow> f : followers) {
			if(f.isPresent()) {
				Follow follower = f.get(); 
				Profile profile = profileRepo.findById(follower.getFolloweeSeq()).get();
				
				List<UserInterest> listUI = userInterestRepo.findAllByUserSeq(profile.getUserSeq());
				List<Integer> listInterest = new ArrayList<Integer>();
				for(UserInterest ui : listUI) {
					listInterest.add(ui.getInterestSeq());
				}
				List<InterestHashtag> listIH = new ArrayList<InterestHashtag>();
				for(Integer i : listInterest) {
					if(interestHashtag.findById(i).isPresent())
						listIH.add(interestHashtag.findById(i).get());
				}
				profile.setInterestHashtags(listIH);
				follower.setProfile(profile);
				followerList.add(follower);
			}			
		}
		return followerList;
	}

	@Override
	public List<Follow> selectFollowee(int seq) {
		List<Optional<Follow>> followees = followRepo.findByFolloweeSeq(seq);
		List<Follow> followeeList = new ArrayList<>();
		for(Optional<Follow> f : followees) {
			if(f.isPresent()) {
				Follow followee = f.get();
				Profile profile = profileRepo.findById(followee.getFollowerSeq()).get(); 
				List<UserInterest> listUI = userInterestRepo.findAllByUserSeq(profile.getUserSeq());
				List<Integer> listInterest = new ArrayList<Integer>();
				for(UserInterest ui : listUI) {
					listInterest.add(ui.getInterestSeq());
				}
				List<InterestHashtag> listIH = new ArrayList<InterestHashtag>();
				for(Integer i : listInterest) {
					if(interestHashtag.findById(i).isPresent())
						listIH.add(interestHashtag.findById(i).get());
				}
				profile.setInterestHashtags(listIH);
				followee.setProfile(profile);
				followeeList.add(followee);
			}
		}
		return followeeList;
	}

	@Override
	public Follow insertFollow(Follow follow) {
		return followRepo.save(follow);
	}

	@Override
	public boolean findFollowing(int followerSeq, int followeeSeq) {
		return followRepo.existsByFollowerSeqAndFolloweeSeq(followerSeq, followeeSeq);
	}

	@Override
	public void deleteFollow(Follow follow) {
		followRepo.delete(follow);
	}
}
