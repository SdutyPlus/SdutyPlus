package com.d108.sduty.controller;

import java.io.DataInput;
import java.io.IOException;
import java.util.*;

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

import com.d108.sduty.dto.Report;
import com.d108.sduty.dto.Task;
import com.d108.sduty.dto.User;
import com.d108.sduty.service.ReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.swagger.annotations.ApiOperation;
import lombok.Data;

@RestController
@RequestMapping("/report")
public class ReportController {
	
	@Autowired
	private ReportService reportService;
	
	@ApiOperation(value = "테스크 등록하기")
	@PostMapping("/tasks")
	public ResponseEntity<?> regist(@RequestBody ObjectNode reqObject) throws IllegalArgumentException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		//객체 역직렬화 가능 업데이트
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		int userSeq = reqObject.get("ownerSeq").intValue();
		String date = reqObject.get("date").asText();
		//리스트 받아오기
		List<Task> tasks = mapper.convertValue(reqObject.get("tasks"), new TypeReference<List<Task>>() {
		});
		Task task = tasks.get(0);
		reportService.registTask(userSeq, date, task);
		return new ResponseEntity<Map<String, Object>>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "리포트 조회")
	@GetMapping("/{user_seq}/{date}")
	public ResponseEntity<?> report(@PathVariable int user_seq, @PathVariable String date){
		Map<String, Object> resultMap = reportService.getReport(user_seq, date);
		if(resultMap==null) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation(value = "테스크 상세조회")
	@GetMapping("/tasks/{task_seq}")
	public ResponseEntity<?> registTask(@PathVariable int task_seq){
		return new ResponseEntity<Task>(reportService.getTask(task_seq), HttpStatus.OK);
	}
	
	@ApiOperation(value = "테스크 수정")
	@PutMapping("/tasks/{task_seq}")
	public ResponseEntity<?> updateTask(@PathVariable int task_seq, @RequestBody Task task){
		Task result = reportService.updateTask(task);
		if(result == null) {
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Task>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value = "테스크 삭제")
	@DeleteMapping("/tasks/{task_seq}")
	public ResponseEntity<?> deleteTask(@PathVariable int task_seq){
		reportService.deleteTask(task_seq);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
