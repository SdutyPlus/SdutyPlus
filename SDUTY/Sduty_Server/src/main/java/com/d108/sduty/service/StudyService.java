package com.d108.sduty.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.d108.sduty.dto.Alarm;
import com.d108.sduty.dto.Study;
import com.d108.sduty.dto.User;

public interface StudyService {
	public List<Study> getAllStudy();
	public boolean checkStudyName(String name);
	public void registStudy(Study study, Alarm alarm);
	public Study getStudyDetail(int studySeq);
	public Set<Study> getMyStudies(int userSeq);
	public Study updateStudy(int user_seq, Study newStudy);
	public boolean deleteStudy(int userSeq, int studySeq);
	public List<Study> filterStudy(String category, boolean emptyfilter, boolean camfilter, boolean publicfilter);
	public List<Study> searchStudy(String keyword);
	public Specification<Study> findCategory(String category);
	public Specification<Study> findEmpty();
	public Specification<Study> findCamStudy(boolean isCamStudy);
	public Specification<Study> findPublic(boolean isPublic);
	public boolean joinStudy(int studySeq, int userSeq);
	public boolean disjoinStudy(int studySeq, int userSeq);
	public String createCron(Alarm alarm);
	public boolean addJob(Study study);
	public boolean deleteJob(Study study);
}
