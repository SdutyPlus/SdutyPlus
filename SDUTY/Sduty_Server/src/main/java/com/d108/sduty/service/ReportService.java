package com.d108.sduty.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.d108.sduty.dto.Task;

@Service
public interface ReportService {
	public void registTask(int userSeq, String date, Task task);
	public Map<String, Object> getReport(int userSeq, String date);
	public Task getTask(int taskSeq);
	public Task updateTask(Task task);
	public void deleteTask(int taskSeq);
}