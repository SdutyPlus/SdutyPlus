//이건 단순 스케쥴링만 가능해서 버림..
//package com.d108.sduty.utils;
//
//import java.util.Date;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//
//import org.springframework.scheduling.Trigger;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
//import org.springframework.scheduling.support.CronTrigger;
//import org.springframework.stereotype.Component;
//
//@Component
//public class StudyScheduler {
//	private ThreadPoolTaskScheduler scheduler;
//	private String cron = "0/10 * * * * *";
//	
//	public void startScheduler(int n) {
//		scheduler = new ThreadPoolTaskScheduler();
//		scheduler.initialize();
//		scheduler.schedule(getRunnable(n), getTrigger());
//	}
//	
//	public void changeCronSet(String cron) {
//		this.cron = cron;
//	}
//	
//	public void stopScheduler() {
//		scheduler.shutdown();
//	}
//	
////	@Scheduled(cron="${this.time}")
////	public void printDate() {
////		System.out.println(new Date().toString());
////	}
//	private Runnable getRunnable(int n) {
//		return ()->{
//			System.out.println(n);
//			System.out.println(new Date().toString());
//		};
//	}
//	
//	private Trigger getTrigger() {
//		return new CronTrigger(cron);
//	}
//	
//	//프로젝트가 구동하자마자 동작
////	@PostConstruct
////	public void init() {
////		startScheduler(0);
////	}
////
////	@PreDestroy
////	public void destroy() {
////		stopScheduler();
////	}
//}