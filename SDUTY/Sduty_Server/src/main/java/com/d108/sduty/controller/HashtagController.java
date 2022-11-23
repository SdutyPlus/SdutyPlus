package com.d108.sduty.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d108.sduty.dto.InterestHashtag;
import com.d108.sduty.dto.JobHashtag;
import com.d108.sduty.service.TagService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "Tag")
@RestController
@RequestMapping("/tag")
public class HashtagController {
	
	@Autowired
	private TagService tagService;
		
	@ApiOperation(value = "모든 관심분야 태그 조회 > List<InterestHashtag> 리턴", response = InterestHashtag.class)
	@GetMapping("/interest")
	public ResponseEntity<?> selectInterestHashtag(){
		List<InterestHashtag> list = tagService.findAllInterest();
		if(list != null) {
			return new ResponseEntity<List<InterestHashtag>>(list, HttpStatus.OK);
		}else {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
	
	@ApiOperation(value = "모든 관심분야 태그 조회 > List<InterestHashtag> 리턴", response = InterestHashtag.class)
	@GetMapping("/job")
	public ResponseEntity<?> selectJobHashtag(){
		List<JobHashtag> list = tagService.findAllJob();
		if(list != null) {
			return new ResponseEntity<List<JobHashtag>>(list, HttpStatus.OK);
		}else {
			return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
		}
	}
	
	
}
