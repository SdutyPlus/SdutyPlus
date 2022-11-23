package com.d108.sduty.service;

import java.util.List;

import com.d108.sduty.dto.Notice;
import com.d108.sduty.dto.Qna;

public interface SettingService {
	public int registQuestion(Qna qna);
	public Qna getQnaDetail(int qnaSeq);
	public Qna updateQuestion(int qnaSeq, Qna qna);
	public void deleteQna(int qnaSeq);
	public List<Notice> getNoticeList();
}
