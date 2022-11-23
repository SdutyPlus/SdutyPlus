package com.d108.sduty.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.d108.sduty.dto.Admin;
import com.d108.sduty.dto.DailyQuestion;
import com.d108.sduty.dto.Notice;
import com.d108.sduty.dto.PagingResult;
import com.d108.sduty.dto.Qna;
import com.d108.sduty.dto.Story;
import com.d108.sduty.dto.User;

public interface AdminService {
	public Optional<Admin> getAdmin(String adminId);
	public boolean registNotice(Notice notice);
	public Notice updateNotice(Notice notice);
	public void deleteNotice(int noticeSeq);
	
	public List<User> getBadUsers();
	public void limitUser();
	//데일리 질문
	public boolean registDailyQuestion(DailyQuestion dailyq);
	public List<DailyQuestion> getDailyQuestions();
	public DailyQuestion getDailyQuestionDetail(int questionSeq);
	public DailyQuestion updateDailyQuestion(DailyQuestion dailyq);
	public void deleteDailyQuestion(int dailyQuestionSeq);
	//1:1문의
	public List<Qna> getQnas();
	public Qna getQnaDetail(int qnaSeq);
	public Qna registAnswer(Qna qna);
	public Qna deleteAnswer(int qnaSeq);
	PagingResult<Story> getBadStories(Pageable pageable);
}
