package com.d108.sduty.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d108.sduty.dto.InterestHashtag;
import com.d108.sduty.dto.JobHashtag;
import com.d108.sduty.repo.InterestRepo;
import com.d108.sduty.repo.JobRepo;

@Service
public class TagServiceImpl implements TagService{
	
	@Autowired
	private InterestRepo interestRepo;
	@Autowired
	private JobRepo jobRepo;
	

	@Override
	public List<InterestHashtag> findAllInterest() {
		return interestRepo.findAll();
	}
	@Override
	public List<JobHashtag> findAllJob() {
		return jobRepo.findAll();
	}
	
}
