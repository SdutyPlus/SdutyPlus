package com.d108.sduty.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d108.sduty.dto.InterestHashtag;
import com.d108.sduty.dto.JobHashtag;
import com.d108.sduty.repo.InterestRepo;

public interface TagService {
	
	List<InterestHashtag> findAllInterest();
	
	List<JobHashtag> findAllJob();
	
}
