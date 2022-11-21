package com.d108.sduty.service;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d108.sduty.dto.InterestHashtag;
import com.d108.sduty.dto.Profile;
import com.d108.sduty.dto.UserInterest;
import com.d108.sduty.repo.FollowRepo;
import com.d108.sduty.repo.InterestHashtagRepo;
import com.d108.sduty.repo.ProfileRepo;
import com.d108.sduty.repo.StoryRepo;
import com.d108.sduty.repo.UserInterestRepo;

@Service
public class ProfileServiceImpl implements ProfileService {
	@Autowired
	private ProfileRepo profileRepo;

	@Autowired
	private UserInterestRepo userInterestRepo;
	
	@Autowired
	private InterestHashtagRepo interestHashtag;
	
	@Autowired
	private StoryRepo storyRepo;
	
	@Autowired
	private FollowRepo followRepo;
	
	@Override
	public Profile insertProfile(Profile profile) throws Exception {
		Profile p = profileRepo.save(profile);
		if(p.getInterestHashtags() != null) {
			if(!profile.getInterestHashtagSeqs().isEmpty()) {
				for(int i : profile.getInterestHashtagSeqs()) {
					userInterestRepo.save(new UserInterest(profile.getUserSeq(), i));
				}
			}
		}
		return p;
	}

	@Override
	public boolean checkDupNickname(String nickname) throws Exception {
		return profileRepo.existsBynickname(nickname);
	}

	@Override
	public Profile selectProfile(int userSeq) {
		List<UserInterest> listUI = userInterestRepo.findAllByUserSeq(userSeq);
		List<Integer> listInterest = new ArrayList<Integer>();
		for(UserInterest ui : listUI) {
			listInterest.add(ui.getInterestSeq());
		}
		List<InterestHashtag> listIH = new ArrayList<InterestHashtag>();
		for(Integer i : listInterest) {
			if(interestHashtag.findById(i).isPresent())
				listIH.add(interestHashtag.findById(i).get());
		}
		Optional<Profile> OProfile =  profileRepo.findById(userSeq);
		Profile p;
		if(OProfile.isPresent()) {
			p = OProfile.get();
			p.setFollowers(followRepo.countByFollowerSeq(userSeq).intValue());
			p.setFollowees(followRepo.countByFolloweeSeq(userSeq).intValue());
			p.setInterestHashtags(listIH);
			p.setCntStory(storyRepo.countAllByWriterSeq(userSeq));
			return p;
		}
		return null;
	}

	@Override
	public Profile selectBaseProfile(int seq) {
		Optional<Profile> p = profileRepo.findById(seq);
		if(p.isPresent())
			return p.get();
		return null;
	}
	
	@Override
	public Profile updateProfile(Profile profile) throws Exception {
		int userSeq = profile.getUserSeq();
		List<UserInterest> listUserInterest = userInterestRepo.findAllByUserSeq(profile.getUserSeq());
		for(UserInterest ui : listUserInterest) {
			userInterestRepo.delete(ui);
		}
		Profile p = profileRepo.save(profile);
		if(p != null) {
			if(!profile.getInterestHashtagSeqs().isEmpty()) {
				for(int i : profile.getInterestHashtagSeqs()) {
					userInterestRepo.save(new UserInterest(userSeq, i));
				}
			}
		}
		return p;
	}

	@Override
	public List<Boolean> selectAllRegtime(int userSeq) {
		List<Boolean> listUploaded = new ArrayList<>();
		for(int i = 0; i < 182; i++) {
			listUploaded.add(false);
		}
		LocalDate now = LocalDate.now();
		int today = now.getDayOfYear();
		List<Integer> listRegtime = storyRepo.findAllRegtime(userSeq);
		for(int i : listRegtime) {
			
			listUploaded.set((181-(today-i))>=0?(181-(today-i)):(181-(today-i)+365), true);
		}
		return listUploaded;
	}

	@Override
	public int changeStudying(int userSeq, int flag) {
		Optional<Profile> OP = profileRepo.findById(userSeq);
		if(OP.isPresent()) {
			Profile p = OP.get();
			p.setIsStudying(flag);
			profileRepo.save(p);
			return p.getIsStudying();
		}
		return -1;
	}

	@Override
	public Profile selectRecommand(int userSeq) {
		Optional<Profile> OP = profileRepo.findById(userSeq);
		Profile p;
		if(OP.isPresent()) {
			p = profileRepo.findRecommanded(OP.get().getJob(), userSeq);
			p.setFollowers(followRepo.countByFollowerSeq(p.getUserSeq()).intValue());
			p.setFollowees(followRepo.countByFolloweeSeq(p.getUserSeq()).intValue());
			return p;
		}
		return null;
	}




}
