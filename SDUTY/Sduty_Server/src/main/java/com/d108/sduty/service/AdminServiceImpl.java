package com.d108.sduty.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.d108.sduty.dto.Admin;
import com.d108.sduty.dto.DailyQuestion;
import com.d108.sduty.dto.Notice;
import com.d108.sduty.dto.PagingResult;
import com.d108.sduty.dto.Qna;
import com.d108.sduty.dto.Story;
import com.d108.sduty.dto.User;
import com.d108.sduty.repo.AdminRepo;
import com.d108.sduty.repo.DailyQuestionRepo;
import com.d108.sduty.repo.NoticeRepo;
import com.d108.sduty.repo.QnaRepo;
import com.d108.sduty.repo.StoryRepo;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepo adminRepo;
	@Autowired
	private NoticeRepo noticeRepo;
	@Autowired
	private DailyQuestionRepo dailyqRepo;
	@Autowired
	private QnaRepo qnaRepo;
	@Autowired
	private StoryRepo storyRepo;
	
	@Override
	public Optional<Admin> getAdmin(String id) {
		return adminRepo.findByid(id);
	}

	@Override
	@Transactional
	public boolean registNotice(Notice notice) {
		Notice newNotice = noticeRepo.save(notice);
		return (newNotice!=null);
	}

	@Override
	@Transactional
	public Notice updateNotice(Notice notice) {
		Optional<Notice> noticeOp = noticeRepo.findById(notice.getSeq());
		if(noticeOp.isPresent()) {
			Notice originNotice = noticeOp.get();
			originNotice.setContent(notice.getContent());
			return noticeRepo.save(originNotice);
		}
		return null;
	}

	@Override
	@Transactional
	public void deleteNotice(int noticeSeq) {
		noticeRepo.deleteById(noticeSeq);
	}

	@Override
	public PagingResult<Story> getBadStories(Pageable pageable) {				
		Page<Story> pageStory = storyRepo.findByWarningGreaterThanOrderByRegtimeDesc(0, pageable);
		System.out.println(pageStory.toList());
		return new PagingResult<Story>(pageable.getPageNumber(), pageStory.getTotalPages(), pageStory.toList());
	}

	@Override
	public List<User> getBadUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void limitUser() {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public boolean registDailyQuestion(DailyQuestion dailyq) {
		DailyQuestion result = dailyqRepo.save(dailyq);
		return (result!=null);
	}

	@Override
	public List<DailyQuestion> getDailyQuestions() {
		return dailyqRepo.findAll();
	}

	@Override
	public DailyQuestion getDailyQuestionDetail(int questionSeq) {
		Optional<DailyQuestion> dq = dailyqRepo.findById(questionSeq);
		if(dq.isPresent()) {
			return dq.get();
		}
		return null;
	}

	@Override
	@Transactional
	public DailyQuestion updateDailyQuestion(DailyQuestion dailyq) {
		Optional<DailyQuestion> dqOp = dailyqRepo.findById(dailyq.getSeq());
		if(dqOp.isPresent()) {
			DailyQuestion origindq = dqOp.get();
			origindq.setContent(dailyq.getContent());
			return dailyqRepo.save(origindq);
		}
		return null;
	}

	@Override
	@Transactional
	public void deleteDailyQuestion(int dailyQuestionSeq) {
		dailyqRepo.deleteById(dailyQuestionSeq);
	}

	@Override
	public List<Qna> getQnas() {
		return qnaRepo.findAll();
	}

	@Override
	public Qna getQnaDetail(int qnaSeq) {
		Optional<Qna> qna = qnaRepo.findById(qnaSeq);
		if(qna.isPresent()) {
			return qna.get();
		}
		return null;
	}

	@Override
	@Transactional
	public Qna registAnswer(Qna qna) {
		Optional<Qna> qnaOp = qnaRepo.findById(qna.getSeq());
		if(qnaOp.isPresent()) {
			Qna originQna = qnaOp.get();
			originQna.setAnswer(qna.getAnswer());
			originQna.setAnsWriter(qna.getAnsWriter());
//			originQna.setAnswerRegtime(LocalDateTime.now());
			return qnaRepo.save(originQna);
		}
		return null;
	}

	@Override
	@Transactional
	public Qna deleteAnswer(int qnaSeq) {
		Optional<Qna> qnaOp = qnaRepo.findById(qnaSeq);
		if(qnaOp.isPresent()) {
			Qna originQna = qnaOp.get();
			originQna.setAnswer(null);
			originQna.setAnsWriter(null);			
			return qnaRepo.save(originQna);
		}
		return null;
	}

}
