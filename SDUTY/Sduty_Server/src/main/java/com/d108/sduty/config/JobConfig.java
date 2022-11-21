package com.d108.sduty.config;
import static org.quartz.JobBuilder.newJob;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.d108.sduty.dto.Job;
import com.d108.sduty.dto.Study;
import com.d108.sduty.service.StudyService;

@Configuration
public class JobConfig {
	
	@Autowired
	private Scheduler scheduler;
	@Autowired
	private StudyService studyService;
	
	@PostConstruct//프로그램 실행 시, 실행
	public void start() {
		//1. 캠스터디 정보 다 들고오기
		List<Study> list = studyService.filterStudy(null, false, true, false);
		//2. scheduler 등록
		for(Study study : list) {
			studyService.addJob(study);
		}
		
		//JobDetail jobDetail = buildJobDetail(Job.class, new HashMap());
		//JobDataMap map1 = new JobDataMap(Collections.singletonMap("num", 1));
//		JobDetail jobDetail = buildJobDetail("alarm1", "Study", 1);
//		JobDetail jobDetail2 = buildJobDetail("alarm2", "Study", 2);
//		try {
//			scheduler.scheduleJob(jobDetail, buildJobTrigger("0/20 * * * * ?"));
//			scheduler.scheduleJob(jobDetail2, buildJobTrigger("0/20 * * * * ?"));
//		} catch(SchedulerException e) {
//			e.printStackTrace();
//		}
	}
	
//	@PreDestroy
//	public void destroy() {
//		try {
//			scheduler.shutdown();
//		} catch (SchedulerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	 public Trigger buildJobTrigger(String scheduleExp){
	        return TriggerBuilder.newTrigger()
	                .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp)).build();
	    }
	
	public JobDetail buildJobDetail(String name, String group, int n) {
		JobDetail jobDetail = JobBuilder.newJob(Job.class)
				.withIdentity(name, group)
				.usingJobData("studySeq", n)
				.withDescription("Study Scheduling")
				.build();
		return jobDetail;
//		JobDataMap jobDataMap = new JobDataMap();
//		jobDataMap.putAll(params);
//		return newJob(job).usingJobData(jobDataMap).build();
	}
}
