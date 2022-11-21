package com.d108.sduty.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d108.sduty.dto.Report;
import com.d108.sduty.dto.Task;
import com.d108.sduty.dto.User;
import com.d108.sduty.repo.ReportRepo;
import com.d108.sduty.repo.TaskRepo;

@Service
public class ReportServiceImpl implements ReportService {
	@Autowired
	private TaskRepo taskRepo;
	@Autowired
	private ReportRepo reportRepo;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void registTask(int userSeq, String date, Task task) {
		// 1. 해당 날짜 report 가져오기(없으면 만들어서 반환)
		Report report = reportRepo.findByDateAndOwnerSeq(date, userSeq);
		//System.out.println(report);
		if (report == null) {
			report = new Report();
			report.setOwnerSeq(userSeq);
			report.setDate(date);
			report = reportRepo.save(report);
		}

		// 2. task에 report번호 등록
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
		int sec = 0;
		try {
			Date sd = sdf.parse(task.getStartTime());
			Date ed = sdf.parse(task.getEndTime());
			sec += ed.getTime() / 1000 - sd.getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		task.setDurationTime(sec);
		task.setReportSeq(report.getSeq());
		taskRepo.save(task);
	}

	@Override
	public Map<String, Object> getReport(int userSeq, String date) {
		Report report = reportRepo.findByDateAndOwnerSeq(date, userSeq);
		if(report==null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
		int sec = 0;
		for (Task task : report.getTask()) {
			sec += task.getDurationTime();
		}
		int hour = sec / 3600;
		int minute = sec % 3600 / 60;
		sec = sec % 3600 % 60;
		String totalTime = String.format("%02d:%02d:%02d", hour, minute, sec);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("reportDate", report.getDate());
		resultMap.put("date", report.getDate());
		resultMap.put("totalTime", totalTime);
		resultMap.put("tasks", report.getTask());

//		System.out.println(totalTime);
		return resultMap;
	}

	@Override
	public Task getTask(int taskSeq) {
		return taskRepo.findBySeq(taskSeq).get();
	}

	@Override
	@Transactional
	public Task updateTask(Task task) {
		if (taskRepo.existsBySeq(task.getSeq())) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
			int sec = 0;
			try {
				Date sd = sdf.parse(task.getStartTime());
				Date ed = sdf.parse(task.getEndTime());
				sec += ed.getTime() / 1000 - sd.getTime() / 1000;
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
			task.setDurationTime(sec);
			return taskRepo.save(task);
		}
		return null;
	}

	@Override
	@Transactional
	public void deleteTask(int taskSeq) {
		taskRepo.deleteBySeq(taskSeq);
	}

}
