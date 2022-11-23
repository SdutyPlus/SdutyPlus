package com.d108.sduty.controller;

import java.util.Arrays;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d108.sduty.dto.Alarm;
import com.d108.sduty.dto.Job;

@RestController
@RequestMapping("/test")
public class TestController {
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	//알람 추가
	@GetMapping("")
	public ResponseEntity<?> addAlarm(Alarm alarm){
		//1. create cron
		String[] time = alarm.getTime().split(":");
		System.out.println(Arrays.toString(alarm.getTime().split(":")));
		StringBuilder cron = new StringBuilder(time[2]+" "+time[1]+" "+time[0]+" ? * ");
		//요일
		if(alarm.isMon()) {cron.append("MON,");}
		if(alarm.isTue()) {cron.append("TUE,");}
		if(alarm.isWed()) {cron.append("WED,");}
		if(alarm.isThu()) {cron.append("THU,");}
		if(alarm.isFri()) {cron.append("FRI,");}
		if(alarm.isSat()) {cron.append("SAT,");}
		if(alarm.isSun()) {cron.append("SUN,");}
		if(cron.charAt(cron.length()-1)!=',') {
			System.out.println("선택한 요일 없으므로 scheduling X");
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}
		String result = cron.substring(0, cron.length()-1)+" *";
		System.out.println(result);
//		System.out.println(schedulerFactoryBean);
		
		//2. Job 추가
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		System.out.println(scheduler);
		
		JobDetail jd = JobBuilder.newJob(Job.class)
				.withIdentity("alarm3", "Study")
				.usingJobData("studySeq", 3)
				.withDescription("Study Scheduling")
				.build();
		try {
			scheduler.scheduleJob(jd, TriggerBuilder.newTrigger()
		            .withSchedule(CronScheduleBuilder.cronSchedule(result)).build());
		} catch(SchedulerException e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

}
