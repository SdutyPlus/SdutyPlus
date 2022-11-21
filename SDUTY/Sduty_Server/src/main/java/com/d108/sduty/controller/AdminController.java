package com.d108.sduty.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.d108.sduty.dto.Admin;
import com.d108.sduty.dto.DailyQuestion;
import com.d108.sduty.dto.Notice;
import com.d108.sduty.dto.PagingResult;
import com.d108.sduty.dto.Qna;
import com.d108.sduty.dto.Story;
import com.d108.sduty.dto.User;
import com.d108.sduty.repo.UserRepo;
import com.d108.sduty.service.AdminService;
import com.d108.sduty.utils.FCMUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private UserRepo userRepo;

	@ApiOperation(value = "로그인")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody ObjectNode objectNode) {
		JsonNode idNode = objectNode.get("id");
		JsonNode passwordNode = objectNode.get("password");
		if (idNode == null || passwordNode == null) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		String id = idNode.asText();
		String password = passwordNode.asText();
		Optional<Admin> adminOp = adminService.getAdmin(id);
		if (adminOp.isPresent()) {
			Admin adminObject = adminOp.get();
			if (password.equals(adminObject.getPassword())) {
				return new ResponseEntity<Admin>(adminObject, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}

	@ApiOperation(value = "공지사항 등록")
	@PostMapping("/notice")
	public ResponseEntity<?> registNotice(@RequestBody Notice newNotice) {
		if (newNotice.getWriterSeq() != null && newNotice.getContent() != null) {
			newNotice.setSeq(null);
			if (adminService.registNotice(newNotice)) {
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}

	@ApiOperation(value = "공지사항 수정")
	@PutMapping("/notice/{notice_seq}")
	public ResponseEntity<?> updateNotice(@PathVariable int notice_seq, @RequestBody Notice notice) {
		if (notice_seq == notice.getSeq()) {
			Notice result = adminService.updateNotice(notice);
			if (result != null) {
				return new ResponseEntity<Notice>(result, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}

	@ApiOperation(value = "공지사항 삭제")
	@DeleteMapping("/notice/{notice_seq}")
	public ResponseEntity<?> deleteNotice(@PathVariable int notice_seq) {
		adminService.deleteNotice(notice_seq);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// 신고 관리
	@ApiOperation(value = "신고된 게시물 목록 조회")
	@GetMapping("/bad-story")
	public ResponseEntity<?> getBadStories(Pageable pageable) {
		return new ResponseEntity<PagingResult<Story>>(adminService.getBadStories(pageable), HttpStatus.OK);
	}

	// 데일리 질문
	@ApiOperation(value = "데일리질문 등록")
	@PostMapping("/question")
	public ResponseEntity<?> registDailyQuestion(@RequestBody DailyQuestion dailyq) {
		if (adminService.registDailyQuestion(dailyq)) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}

	@ApiOperation(value = "데일리질문 목록")
	@GetMapping("/question")
	public ResponseEntity<?> getDailyQuestions() {
		return new ResponseEntity<List<DailyQuestion>>(adminService.getDailyQuestions(), HttpStatus.OK);
	}

	@ApiOperation(value = "데일리질문 상세 조회")
	@GetMapping("/question/{question_seq}")
	public ResponseEntity<?> getDailyQuestionDetail(@PathVariable int question_seq) {
		DailyQuestion dq = adminService.getDailyQuestionDetail(question_seq);
		if (dq != null) {
			return new ResponseEntity<DailyQuestion>(dq, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}

	@ApiOperation(value = "데일리질문 수정")
	@PutMapping("/question")
	public ResponseEntity<?> updateDailyQuestion(@RequestBody DailyQuestion dailyq) {
		DailyQuestion dq = adminService.updateDailyQuestion(dailyq);
		return new ResponseEntity<DailyQuestion>(dq, HttpStatus.OK);
	}

	@ApiOperation(value = "데일리질문 삭제")
	@DeleteMapping("/question/{question_seq}")
	public ResponseEntity<?> deleteDailyQustion(@PathVariable int question_seq) {
		adminService.deleteDailyQuestion(question_seq);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// 1:1 문의
	@ApiOperation(value = "1:1문의 목록 조회")
	@GetMapping("/qna")
	public ResponseEntity<?> getQnas() {
		List<Qna> list = adminService.getQnas();
		List<Qna> newList = new ArrayList<Qna>();
		for (Qna q : list) {
			if (q.getAnsWriter().isEmpty()) {
				newList.add(q);
			}
		}
		return new ResponseEntity<List<Qna>>(newList, HttpStatus.OK);
	}

	@ApiOperation(value = "1:1문의 상세 조회")
	@GetMapping("/qna/{qna_seq}")
	public ResponseEntity<?> getQnaDetail(@PathVariable int qna_seq) {
		Qna qna = adminService.getQnaDetail(qna_seq);
		if (qna != null) {
			return new ResponseEntity<Qna>(qna, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}

	@ApiOperation(value = "1:1문의 답변 등록 및 수정")
	@PostMapping("/qna")
	public ResponseEntity<?> registAnswer(@RequestBody Qna qna) {
		Qna result = adminService.registAnswer(qna);
		if (result != null) {
			return new ResponseEntity<Qna>(result, HttpStatus.OK);
		}

		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}

	@ApiOperation(value = "1:1문의 답변 삭제")
	@PutMapping("/qna/{qna_seq}")
	public ResponseEntity<?> updateAnswer(@PathVariable int qna_seq) {
		Qna result = adminService.deleteAnswer(qna_seq);
		if (result != null) {
			return new ResponseEntity<Qna>(result, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}

	@PostMapping("/fcm")
	public ResponseEntity<?> sendFCM(@RequestBody Map<String, String> message) {
		List<User> userList = userRepo.findAllByUserPublicGreaterThan(1);
		FCMUtil fcmUtil = new FCMUtil();
		List<String> tokenList = new ArrayList<String>();
		for (User user : userList) {
			tokenList.add(user.getFcmToken());
		}
		fcmUtil.send_FCM_All(tokenList, message);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@GetMapping("/fcm/{userSeq}")
	public ResponseEntity<?> sendFCMOne(@PathVariable int userSeq) {
		Optional<User> user = userRepo.findBySeq(userSeq);

		FCMUtil fcmUtil = new FCMUtil();
		if (user.isPresent()) {
			if (user.get().getUserPublic() > 0) {
				fcmUtil.send_FCM(user.get().getFcmToken(), "SDUTY", "1:1 문의 답변이 등록되었습니다.");
				return new ResponseEntity<Void>(HttpStatus.OK);
			}
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);

	}
}
