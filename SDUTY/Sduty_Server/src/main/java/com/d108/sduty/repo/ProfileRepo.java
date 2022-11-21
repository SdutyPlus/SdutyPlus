package com.d108.sduty.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.d108.sduty.dto.Profile;
import com.d108.sduty.dto.Story;

public interface ProfileRepo extends JpaRepository<Profile, Integer>{
	boolean existsBynickname(String nickname);
	
	@Query(value="select * from profile as p where profile_public_job = 2 and profile_job = ?1 and profile_user_seq != ?2 order by rand() limit 1", nativeQuery=true)
	Profile findRecommanded(String profileJob, int mySeq);
}
