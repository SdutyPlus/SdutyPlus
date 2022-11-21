package com.d108.sduty.repo;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.d108.sduty.dto.Follow;

@Repository
public interface FollowRepo extends JpaRepository<Follow, Integer>{
	boolean existsByFollowerSeqAndFolloweeSeq(int followerSeq, int followeeSeq);
	@Transactional
	void deleteByFollowerSeqAndFolloweeSeq(int followerSeq, int followeeSeq);
	List<Optional<Follow>> findByFollowerSeq(int seq);
	List<Optional<Follow>> findByFolloweeSeq(int seq);
	Long countByFollowerSeq(int followerSeq);
	Long countByFolloweeSeq(int followeeSeq);
}
