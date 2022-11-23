package com.d108.sduty.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.d108.sduty.dto.Alarm;
import com.d108.sduty.dto.Job;
import com.d108.sduty.dto.Study;
import com.d108.sduty.dto.User;
import com.d108.sduty.repo.AlarmRepo;
import com.d108.sduty.repo.InterestHashtagRepo;
import com.d108.sduty.repo.StudyRepo;
import com.d108.sduty.repo.UserRepo;

@Service
public class StudyServiceImpl implements StudyService {

	@Autowired
	private StudyRepo studyRepo;
	@Autowired
	private AlarmRepo alarmRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Override
	public List<Study> getAllStudy() {
		return studyRepo.findAll();
	}

	@Override
	public boolean checkStudyName(String name) {
		return studyRepo.existsByName(name);
	}

	@Override
	@Transactional
	public void registStudy(Study study, Alarm alarm) {
		//1. 방장 참여
		study.getParticipants().add(userRepo.findBySeq(study.getMasterSeq()).get());
		if(alarm!=null) {
			alarm.setCron(createCron(alarm));
			alarm = alarmRepo.save(alarm);
			study.setAlarm(alarm);
			//스케쥴러 동작
			addJob(study);
		}
		studyRepo.save(study);
	}

	@Override
	public Study getStudyDetail(int studySeq) {
		return studyRepo.findBySeq(studySeq).get();
	}
	

	@Override
	public Set<Study> getMyStudies(int userSeq) {
		Optional<User> user = userRepo.findBySeq(userSeq);
		if(user.isPresent()) {
			return user.get().getStudies();
		}
		return null;
	}
	

	@Override
	@Transactional
	public Study updateStudy(int userSeq, Study newStudy) {
		Study originStudy = getStudyDetail(newStudy.getSeq());
		//유효한 스터디 & 방장만 수정 가능
		if(originStudy!=null && userSeq == originStudy.getMasterSeq()) {
			if(!originStudy.getName().equals(newStudy.getName())) {
				//이름 중복 체크
				if(checkStudyName(newStudy.getName())) { return null;}
				if(originStudy.getRoomId()!=null) {
					deleteJob(originStudy);
				}
				originStudy.setName(newStudy.getName());
				if(originStudy.getRoomId()!=null) {
					addJob(originStudy);
				}
			}
			if(originStudy.getLimitNumber()!=newStudy.getLimitNumber()) {
				//현재 참여 인원보다 적게 신청했을 경우
				if(originStudy.getParticipants().size()>newStudy.getLimitNumber()) { return null;}
				originStudy.setLimitNumber(newStudy.getLimitNumber());
			}
			if(originStudy.getRoomId()!=null && newStudy.getAlarm()!=null) {
				//캠스터디
				deleteJob(originStudy);
				newStudy.getAlarm().setCron(createCron(newStudy.getAlarm()));
				originStudy.setAlarm(newStudy.getAlarm());
				alarmRepo.save(newStudy.getAlarm());
				addJob(originStudy);
			}
			if(newStudy.getNotice()!=null) originStudy.setNotice(newStudy.getNotice());
			if(newStudy.getMasterSeq()!=0) originStudy.setMasterSeq(newStudy.getMasterSeq());
			if(newStudy.getCategory()!=null) originStudy.setCategory(newStudy.getCategory());
			originStudy.setPassword(newStudy.getPassword());
			originStudy.setIntroduce(newStudy.getIntroduce());
			
			Study result = studyRepo.save(originStudy);
			//System.out.println(result.getCategories());
			return result;
		}
		return null;
	}

	@Override
	@Transactional
	public boolean deleteStudy(int userSeq, int studySeq) {
		//1. user_seq가 방장인지 확인
		Study study = studyRepo.findBySeq(studySeq).get();
		if(study.getMasterSeq() != userSeq) {
			return false;
		}
		//2. 삭제
		if(study.getRoomId()!=null) {
			deleteJob(study);
			alarmRepo.delete(study.getAlarm());
		}
		if(studyRepo.deleteBySeq(studySeq)==0) {
			return false;
		}
		return true;
	}

	@Override
	public List<Study> filterStudy(String category, boolean emptyfilter, boolean camfilter, boolean publicfilter) {
		Specification<Study> spec = (root, query, criteriaBuilder)->null;
		if(category!=null && !category.equals("전체")) {
			spec = spec.and(findCategory(category));
		}
		if(emptyfilter==true) {
			spec = spec.and(findEmpty());
		}
		if(camfilter==true) {
			spec = spec.and(findCamStudy(camfilter));
		}
		if(publicfilter==true) {
			spec = spec.and(findPublic(publicfilter));
		}
		return studyRepo.findAll(spec);
	}
	
	@Override
	public List<Study> searchStudy(String keyword){
		Specification<Study> spec = (root, query, criteriaBuilder)->criteriaBuilder.like(root.get("name"), "%"+keyword+"%");
		return studyRepo.findAll(spec);
	}
	
	//TODO : category 필터링 수정 필요
	public Specification<Study> findCategory(String category){
		return (root, query, criteriaBuilder)->criteriaBuilder.equal(root.get("category"), category);
	}
	
	public Specification<Study> findEmpty(){
		return (root, query, criteriaBuilder)->criteriaBuilder.gt(root.get("limitNumber"), root.get("joinNumber"));
	}
	
	public Specification<Study> findCamStudy(boolean isCamStudy){
		return (root, query, criteriaBuilder)->criteriaBuilder.isNotNull(root.get("roomId"));
	}
	
	public Specification<Study> findPublic(boolean isPublic){
		if(isPublic) {
			return (root, query, criteriaBuilder)->criteriaBuilder.isNull(root.get("password"));
		}
		else {//여긴 올 일 없음 (공개/전체) 선택지만 있음
			return (root, query, criteriaBuilder)->criteriaBuilder.isNotNull(root.get("password"));
		}
		
	}

	@Override
	@Transactional
	public boolean joinStudy(int studySeq, int userSeq) {
		Study study = studyRepo.findBySeq(studySeq).get();
		User user = userRepo.findBySeq(userSeq).get();
		//이미 참여 중이면
		if(study.getParticipants().contains(user)) {
			System.out.println("이미 참여중");
			return false;
		}
		//참여 study, user 한 쪽만 해주면, N:M관계에 따라 양방향 자동 적용됨.
		study.getParticipants().add(user);
		studyRepo.save(study);
		return true;
	}

	@Override
	@Transactional
	public boolean disjoinStudy(int studySeq, int userSeq) {
		System.out.println(studySeq+", "+userSeq);
		Study study = studyRepo.findBySeq(studySeq).get();
		User user = userRepo.findBySeq(userSeq).get();
		//1. 참여 중인가? & 방장은 탈퇴X 삭제만 가능
		if(!study.getParticipants().contains(user)) {
			System.out.println("스터디 회원이 아니므로 탈퇴할 수 없습니다.");
			return false;
		}
		//2. 탈퇴
		study.getParticipants().remove(user);
		studyRepo.save(study);
		return true;
	}

	@Override
	public String createCron(Alarm alarm) {
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
		if(cron.charAt(cron.length()-1)==',') {
			String result = cron.substring(0, cron.length()-1)+" *";
			return result;
		}
		System.out.println("선택한 요일 없으므로 scheduling X");
		return null;
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public boolean addJob(Study study) {
		String cron = study.getAlarm().getCron();
		if(cron==null) {
			return false;
		}
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
		JobDetail jd = JobBuilder.newJob(Job.class)
				.withIdentity(study.getName(), "Study")
				.usingJobData("studySeq", study.getSeq())
				.withDescription("Study Scheduling")
				.build();
		try {
			scheduler.scheduleJob(jd, TriggerBuilder.newTrigger()
		            .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build());
		} catch(SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteJob(Study study) {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobDetail jd = JobBuilder.newJob(Job.class)
				.withIdentity(study.getName(), "Study")
				.usingJobData("studySeq", study.getSeq())
				.withDescription("Study Scheduling")
				.build();
		JobKey jobKey = jd.getKey();
		try {
			scheduler.deleteJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
