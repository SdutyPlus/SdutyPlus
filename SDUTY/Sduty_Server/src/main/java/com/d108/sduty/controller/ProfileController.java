package com.d108.sduty.controller;

import java.time.LocalDate;
import java.time.Year;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.d108.sduty.dto.UserAchieve;
import com.d108.sduty.repo.ProfileRepo;
import com.d108.sduty.dto.Achievement;
import com.d108.sduty.dto.Follow;
import com.d108.sduty.dto.Profile;
import com.d108.sduty.dto.Timeline;
import com.d108.sduty.service.FollowService;
import com.d108.sduty.service.ImageService;
import com.d108.sduty.service.ProfileService;
import com.d108.sduty.service.UserAchieveService;
import com.google.gson.Gson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(value = "Profile")
@RestController
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	private ProfileService profileService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private FollowService followService;
	
	@Autowired
	private UserAchieveService userAchieveService;
	
	@Transactional
	@ApiOperation(value = "프로필 저장 > Profile > Profile 리턴", response = Profile.class)
	@PostMapping("")
	public ResponseEntity<?> insertProfile(@RequestParam("uploaded_file") MultipartFile imageFile,  @RequestParam("profile") String json) throws Exception {
		Gson gson = new Gson();
		System.out.println(gson);
		Profile profile = gson.fromJson(json, Profile.class);
		profile.setImage(imageFile.getOriginalFilename());
		imageService.fileUpload(imageFile);
		Profile result = profileService.insertProfile(profile);
		if(result != null) {
			return new ResponseEntity<Profile>(profile, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ApiOperation(value = "닉네임 중복 확인 > Nickname > Profile 리턴", response = HttpStatus.class)
	@GetMapping("/check/{nickname}")
	public ResponseEntity<?> checkDupNickname(@PathVariable String nickname) throws Exception {
		boolean result = profileService.checkDupNickname(nickname);
		if(result) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ApiOperation(value = "프로필 조회 > UserSeq > Profile 리턴", response = Profile.class)
	@GetMapping("/{userSeq}")
	public ResponseEntity<?> selectProfile(@PathVariable int userSeq) {
		Profile selectedProfile = profileService.selectProfile(userSeq);
		if(selectedProfile != null) {
			return new ResponseEntity<Profile>(selectedProfile, HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
	}
	
	@Transactional
	@ApiOperation(value = "프로필 수정 > UserSeq > Profile 리턴", response = Profile.class)
	@PutMapping("")
	public ResponseEntity<?> updateProfile(@RequestBody Profile profile) throws Exception {
		Profile result = profileService.updateProfile(profile);
		if(result != null) {
			return new ResponseEntity<Profile>(result, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@Transactional
	@ApiOperation(value = "프로필 수정(사진) > UserSeq > Profile 리턴", response = Profile.class)
	@PutMapping("/image")
	public ResponseEntity<?> updateProfileImage(@RequestParam("uploaded_file") MultipartFile imageFile,  @RequestParam("profile") String json) throws Exception {
		Gson gson = new Gson();
		Profile profile = gson.fromJson(json, Profile.class);
		imageService.deleteFile(profileService.selectProfile(profile.getUserSeq()).getImage());
		profile.setImage(imageFile.getOriginalFilename());
		imageService.fileUpload(imageFile);
		Profile result = profileService.updateProfile(profile);
		if(result != null) {
			return new ResponseEntity<Profile>(profile, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ApiOperation(value = "팔로워 조회 > UserSeq > 팔로워 리턴", response = Follow.class)
	@GetMapping("/follower/{userSeq}")
	public ResponseEntity<?> selectFollower(@PathVariable int userSeq) {
		List<Follow> followers = followService.selectFollower(userSeq);
		if(followers!=null) {
			return new ResponseEntity<List<Follow>>(followers, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}

	@ApiOperation(value = "팔로이 조회 > UserSeq > 팔로이 리턴", response = Follow.class)
	@GetMapping("/followee/{userSeq}")
	public ResponseEntity<?> selectFollowee(@PathVariable int userSeq) {
		List<Follow> followees = followService.selectFollowee(userSeq);
		if(followees!=null) {
			return new ResponseEntity<List<Follow>>(followees, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@Transactional
	@ApiOperation(value = "유저 신고 > UserSeq > HttpStatus", response = HttpStatus.class)
	@PutMapping("/warning/{userSeq}")
	public ResponseEntity<?> warnUser(@PathVariable int userSeq) throws Exception {
		Profile selectedProfile = profileService.selectProfile(userSeq);
		if(selectedProfile != null) {
			Profile updatingProfile = selectedProfile;
			updatingProfile.setWarning(updatingProfile.getWarning() + 1);
			profileService.updateProfile(updatingProfile);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@Transactional
	@ApiOperation(value = "업적 달성 > Achievement > ", response = HttpStatus.class)
	@PostMapping("/achievement")
	public ResponseEntity<?> achieveAchievement(@RequestBody UserAchieve userAchieve) {
		int result = userAchieveService.insertUserAchieve(userAchieve.getUserSeq(), userAchieve.getAchievementSeq());
		if(result > 0) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	
	@ApiOperation(value = "유저 달성 업적 조회 > Achievement > ", response = Achievement.class)
	@GetMapping("/achievement/{userSeq}")
	public ResponseEntity<?> selectUserAchievement(@PathVariable int userSeq) {
		List<Achievement> selectedAchievement = userAchieveService.selectUserAchieve(userSeq); 
		if(selectedAchievement != null) {
			return new ResponseEntity<List<Achievement>>(selectedAchievement, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ApiOperation(value = "업적 상세 정보 조회 > UserAchieve > ", response = Achievement.class)
	@GetMapping("/achievement/info/{achieveSeq}")
	public ResponseEntity<?> selectAchievementInfo(@PathVariable int achieveSeq) {
		Achievement selectedAchievement = userAchieveService.selectAchievement(achieveSeq); 
		if(selectedAchievement != null) {
			return new ResponseEntity<Achievement>(selectedAchievement, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@Transactional
	@ApiOperation(value = "대표 업적 설정 > Achievement > ", response = Achievement.class)
	@PutMapping("/achievement")
	public ResponseEntity<?> updateRepAchievement(@RequestBody UserAchieve userAchieve) throws Exception {
		Profile selectedProfile = profileService.selectProfile(userAchieve.getUserSeq());
		if(selectedProfile != null) {
			Profile profile = selectedProfile;
			profile.setMainAchievmentSeq(userAchieve.getAchievementSeq());
			Profile tempProfile = profileService.updateProfile(profile);
			if(tempProfile != null) {
				return new ResponseEntity<Void> (HttpStatus.OK);
			}
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@Transactional
	@ApiOperation(value = "팔로우/취소 > Follow > HttpStatus", response = HttpStatus.class)
	@PostMapping("/follow")
	public ResponseEntity<?> doFollow(@RequestBody Follow follow) throws Exception {
		int followerSeq = follow.getFollowerSeq();
		int followeeSeq = follow.getFolloweeSeq();
		System.out.println(follow);
		boolean alreadyFollowing = followService.findFollowing(followerSeq, followeeSeq);
		Follow result;
		if(alreadyFollowing) {
			try {
				followService.deleteFollow(follow);
			} catch (Exception e) {
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			}
		} else {
			result = followService.insertFollow(follow);
			if(result != null) {			
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "날짜별 유저 스토리 게시 여부 (잔디차트) > UserSeq > List<Boolean>", response = Boolean.class)
	@GetMapping("/chart/{userSeq}")
	public ResponseEntity<?> getGrassChart(@PathVariable int userSeq){
		try {
			return new ResponseEntity<List<Boolean>>(profileService.selectAllRegtime(userSeq), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
	}

	@Transactional
	@ApiOperation(value = "유저 공부중 상태 변경 > UserSeq > int", response = Boolean.class)
	@PutMapping("/timer/{userSeq}/{flag}")
	public ResponseEntity<?> changeIsStudying(@PathVariable int userSeq, @PathVariable int flag){
		int result = profileService.changeStudying(userSeq, flag);
		if(result != -1) {
			if(result == 1)
				return new ResponseEntity<Boolean>(true, HttpStatus.OK);
			else
				return new ResponseEntity<Boolean>(false, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ApiOperation(value = "추천 사용자 조회(유저 시퀀스로) : JobHashtag > Profile 리턴", response = Profile.class)
	@GetMapping("/recommand/{userSeq}")
	public ResponseEntity<?> selectRecommand(@PathVariable int userSeq) throws Exception {
		try {
			Profile p = profileService.selectRecommand(userSeq);
			if(p != null) {
				return new ResponseEntity<Profile>(p, HttpStatus.OK);
			}
		} catch (Exception e) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
}
