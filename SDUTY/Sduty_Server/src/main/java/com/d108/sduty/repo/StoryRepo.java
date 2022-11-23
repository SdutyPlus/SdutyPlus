package com.d108.sduty.repo;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.d108.sduty.dto.Story;

public interface StoryRepo extends JpaRepository<Story, Integer>{
	List<Optional<Story>> findRegtimeBywriterSeq(String userSeqs);//개인 타임라인 조회
	Page<Story> findBywriterSeqOrderByRegtimeDesc(int userSeq, Pageable pageable);//작성자로 조회
	Page<Story> findAllByWriterSeqInOrderByRegtimeDesc(List<Integer> writerSeqs, Pageable pageable);//팔로우한 작성자들로 조회
	Page<Story> findAllByWriterSeqInAndJobHashtagOrderByRegtimeDesc(List<Integer> writerSeqs, int jobSeq, Pageable pageable);//팔로우한 작성자들을 직업 태그로 조회
	Page<Story> findAllByjobHashtagOrderByRegtimeDesc(int jobSeq, Pageable pageable);
	List<Story> findAllByjobHashtagInOrderByRegtimeDesc(List<Integer> seqs);
	Page<Story> findAllBySeqInOrderByRegtimeDesc(List<Integer> storySeqs, Pageable pageable);//게시글 목록들로 조회
	Story findTopByWriterSeqOrderByRegtimeDesc(int writerSeq);
	int countAllByWriterSeq(int writerSeq);
	@Query(value="SELECT distinct dayofyear(substr(story_regtime, 1,10)) as dayofyear FROM sduty.story where story_writer_seq = ?1", nativeQuery=true)
	List<Integer> findAllRegtime(int userSeq);
	
	Page<Story> findByWarningGreaterThanOrderByRegtimeDesc(int zero, Pageable pageable);
	List<Story> findByWarningGreaterThan(int zero);
	@Query(value="select * from story as s where story_public = 2 and story_job_hashtag = ?1 and datediff(now(), story_regtime) < 7 order by rand() limit 1", nativeQuery=true)
	Story findRecommanded(String storyJobHashtag);
	Page<Story> findAllByOrderByRegtimeDesc(Pageable pageable);
	

}
