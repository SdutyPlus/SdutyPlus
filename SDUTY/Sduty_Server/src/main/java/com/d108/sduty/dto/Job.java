package com.d108.sduty.dto;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.d108.sduty.service.StudyService;
import com.d108.sduty.utils.FCMUtil;

@Component
public class Job extends QuartzJobBean{
	//private static final Logger log = LoggerFactory.getLogger(Job.class);

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	@Autowired
	private StudyService studyService;
	@Autowired
	private FCMUtil fcmUtil;
	
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("push알림");
        int studySeq = (int) jobExecutionContext.getJobDetail().getJobDataMap().get("studySeq");
        System.out.println(studyService.getStudyDetail(studySeq).getParticipants());
        //토큰이 없는 사람 = 로그아웃 중
        for(User user : studyService.getStudyDetail(studySeq).getParticipants()) {
        	if(user.getFcmToken()!=null && user.getUserPublic()!=0) {
        		fcmUtil.send_FCM(user.getFcmToken(), "스터디", "스터디 일정 알림");
        	}
        }
 
    }
    
    public JobDetail buildJobDetail(String name, String group, int n) {
		JobDetail job = JobBuilder.newJob(Job.class)
				.withIdentity(name, group)
				.usingJobData("studySeq", n)
				.withDescription("Study Scheduling")
				.build();
		return job;
	}

}
