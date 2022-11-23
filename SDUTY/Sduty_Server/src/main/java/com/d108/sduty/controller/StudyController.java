package com.d108.sduty.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d108.sduty.dto.Alarm;
import com.d108.sduty.dto.Profile;
import com.d108.sduty.dto.Study;
import com.d108.sduty.dto.User;
import com.d108.sduty.service.ProfileService;
import com.d108.sduty.service.ReportService;
import com.d108.sduty.service.StudyService;
import com.d108.sduty.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/study")
public class StudyController {
	
	@Autowired
	private StudyService studyService;
	@Autowired
	private UserService userService;
	@Autowired
	private ProfileService profileService;
	@Autowired
	private ReportService reportService;
	
	@ApiOperation(value = "스터디 등록")
	@PostMapping("")
	public ResponseEntity<?> registStudy(@RequestBody ObjectNode reqObject){
		ObjectMapper mapper = new ObjectMapper();
		//mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		Study study = null;
		Alarm alarm = null;
		try {
			study = mapper.treeToValue(reqObject.get("Study"), Study.class);
			JsonNode node = reqObject.get("Alarm");
			if((study.getRoomId()!=null && node==null)||(study.getRoomId()==null&& node!=null)) {
				//캠스터디인데 알람이 없는경우 || 캠스터디 아닌데 알람이 있는 경우
				return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
			}
			if(node != null) {
				alarm = mapper.treeToValue(node, Alarm.class);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		
		//1. form 제출 후, 이름 중복 검사
		boolean result = studyService.checkStudyName(study.getName());
		if(result==true) {//중복
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		//2. 등록
		studyService.registStudy(study, alarm);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "스터디명 중복검사")
	@GetMapping("/check/{study_name}")
	public ResponseEntity<?> checkStudyName(@PathVariable String study_name){
		boolean result = studyService.checkStudyName(study_name);
		if(result==true) {//중복
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@ApiOperation(value="스터디 전체 조회")
	@GetMapping("")
	public ResponseEntity<?> getAllStudy(){
		List<Study> studies = studyService.getAllStudy();
		if(studies==null) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<List<Study>>(studies, HttpStatus.OK);
	}
	
	@ApiOperation(value="스터디 상세 조회")
	@GetMapping("/detail/{study_seq}")
	public ResponseEntity<?> getStudyDetail(@PathVariable int study_seq){
		Study study = studyService.getStudyDetail(study_seq);
		Profile masterProfile = profileService.selectBaseProfile(study.getMasterSeq());
		study.setMasterNickname(masterProfile.getNickname());
		study.setMasterJob(masterProfile.getJob());
		return new ResponseEntity<Study>(study, HttpStatus.OK);
	}
	
	@ApiOperation(value="스터디 참여")
	@PostMapping("/participation/{study_seq}/{user_seq}")
	public ResponseEntity<?> joinStudy(@PathVariable int study_seq, @PathVariable int user_seq){
		if(!studyService.joinStudy(study_seq, user_seq)) {
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@ApiOperation(value="스터디 탈퇴 & 강퇴")
	@DeleteMapping("/participation/{study_seq}/{user_seq}")
	public ResponseEntity<?> disjoinStudy(@PathVariable int study_seq, @PathVariable int user_seq){
		if(!studyService.disjoinStudy(study_seq, user_seq)) {
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@ApiOperation(value = "내 스터디 목록")
	@GetMapping("/{user_seq}")
	public ResponseEntity<?> getMyStudies(@PathVariable int user_seq){
		Set<Study> result = studyService.getMyStudies(user_seq);
		if(result==null) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<Set<Study>>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "스터디 수정")
	@PutMapping("/{user_seq}/{study_seq}")
	public ResponseEntity<?> updateStudy(@PathVariable int user_seq, @PathVariable int study_seq, @RequestBody Study newStudy){
		System.out.println(newStudy);
		if(study_seq==newStudy.getSeq()) {
			Study result = studyService.updateStudy(user_seq, newStudy);
			if(result!=null) {
				Profile masterProfile = profileService.selectBaseProfile(result.getMasterSeq());
				result.setMasterNickname(masterProfile.getNickname());
				result.setMasterJob(masterProfile.getJob());
				return new ResponseEntity<Study>(result, HttpStatus.OK);
			}	
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ApiOperation(value="내 스터디 상세 조회")
	@GetMapping("/{user_seq}/{study_seq}")
	public ResponseEntity<?> getMyStudyDetail(@PathVariable int user_seq, @PathVariable int study_seq) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		//1. 스터디 기본 정보
		Study study = studyService.getStudyDetail(study_seq);
		resultMap.put("study", study);
		//2. 참여자
		//내가 참여자인지 확인
		User user = userService.selectUser(user_seq).get();
		if(!study.getParticipants().contains(user)) {//참여자가 아니면
			return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);//요청거부
		}
		//참여자 정보
		List<Map<String, Object>> list = new ArrayList<>();
		Set<User> participants = study.getParticipants();
		for(User participant : participants) {
			Map<String, Object> user_info = new HashMap<>();
			user_info.put("userSeq", participant.getSeq());
			
			Profile profile = profileService.selectProfile(participant.getSeq());
			if(profile!=null) {
				user_info.put("nickname", profile.getNickname());
				user_info.put("is_studying", profile.getIsStudying());
			}
			else {
				user_info.put("nickname", "프로필 없음");
				user_info.put("is_studying", 0);
			}
			
			Map<String, Object> report = reportService.getReport(participant.getSeq(), LocalDate.now().toString());			
			if(report==null) {
				user_info.put("total_time", "00:00:00");
			}
			else {				
				user_info.put("total_time", report.get("totalTime"));
			}
			
			list.add(user_info);
		}
		resultMap.put("members", list);		
		//3. 캠스터디면, 알람
//		if(study.isCamstudy()) {
//			Alarm alarm = studyService.getAlarm(study_seq);
//			resultMap.put("alarm", alarm);
//		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation(value = "스터디 삭제")
	@DeleteMapping("/{user_seq}/{study_seq}")
	public ResponseEntity<?> deleteStudy(@PathVariable int user_seq, @PathVariable int study_seq){
		Map<String, Object> map = new HashMap<String, Object>();
		if(studyService.deleteStudy(user_seq, study_seq)) {
			map.put("result", "삭제되었습니다.");
		}
		else {
			map.put("result", "삭제할 수 없습니다.");
			return new ResponseEntity<Map<String, Object>>(map, HttpStatus.FORBIDDEN);
		}
		return new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);
	}
	
	@ApiOperation(value = "스터디 필터링")
	@GetMapping("filter/{category}/{emptyfilter}/{camfilter}/{publicfilter}")
	public ResponseEntity<?> filterStudy(@PathVariable String category, @PathVariable boolean emptyfilter, @PathVariable boolean camfilter, @PathVariable boolean publicfilter){
		List<Study> resultList = studyService.filterStudy(category, emptyfilter, camfilter, publicfilter);
		if(resultList==null) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<List<Study>>(resultList, HttpStatus.OK);
	}
	
	@ApiOperation(value = "스터디 검색")
	@GetMapping("filter/{keyword}")
	public ResponseEntity<?> searchStudy(@PathVariable String keyword){
		if(keyword=="") {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		List<Study> resultList = studyService.searchStudy(keyword);
		if(resultList==null) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<List<Study>>(resultList, HttpStatus.OK);
	}
}
