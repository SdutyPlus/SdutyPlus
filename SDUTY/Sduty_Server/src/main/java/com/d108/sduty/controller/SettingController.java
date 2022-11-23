package com.d108.sduty.controller;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.d108.sduty.dto.Notice;
import com.d108.sduty.dto.Qna;
import com.d108.sduty.dto.User;
import com.d108.sduty.repo.QnaRepo;
import com.d108.sduty.service.SettingService;
import com.d108.sduty.service.UserService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/setting")
public class SettingController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private SettingService settingService;
	@Autowired
	private QnaRepo qnaRepo;
	
	@ApiOperation(value = "내 문의사항 목록 조회")
	@GetMapping("/qna/{user_seq}")
	public ResponseEntity<?> getQnaList(@PathVariable int user_seq) throws Exception{
		List<Qna> list = qnaRepo.findAllByUserSeqOrderBySeqDesc(user_seq);
		System.out.println(list);
		if(list != null) {
			return new ResponseEntity<List<Qna>>(list, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ApiOperation(value = "문의사항 작성하기")
	@PostMapping("/qna")
	public ResponseEntity<?> registQna(@RequestBody Qna qna){
		if(settingService.registQuestion(qna)==1) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ApiOperation(value="문의사항 상세보기")
	@GetMapping("/qna/detail/{qna_seq}")
	public ResponseEntity<?> getQnaDetail(@RequestParam int qna_seq){
		Qna qna = settingService.getQnaDetail(qna_seq);
		if(qna!=null) {
			return new ResponseEntity<Qna>(qna, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ApiOperation(value="문의사항 수정하기")
	@PutMapping("/qna/{qna_seq}")
	public ResponseEntity<?> updateQna(@RequestParam int qna_seq, @RequestBody Qna qna){
		qna = settingService.updateQuestion(qna_seq, qna);
		if(qna!=null) {
			return new ResponseEntity<Qna>(qna, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
	}
	
	@ApiOperation(value="문의사항 삭제하기")
	@DeleteMapping("/qna/{qna_seq}")
	public ResponseEntity<?> deleteQna(@RequestParam int qna_seq){
		settingService.deleteQna(qna_seq);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
	@ApiOperation(value="공지사항 목록 조회")
	@GetMapping("/notice")
	public ResponseEntity<?> getNoticeList(){
		List<Notice> notices = settingService.getNoticeList();
		return new ResponseEntity<List<Notice>>(notices, HttpStatus.OK);
	}
}
