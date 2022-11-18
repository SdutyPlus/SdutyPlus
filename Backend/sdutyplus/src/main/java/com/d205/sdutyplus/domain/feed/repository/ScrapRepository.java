package com.d205.sdutyplus.domain.feed.repository;

import com.d205.sdutyplus.domain.feed.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    Optional<Scrap> findByUserSeqAndFeedSeq(Long userSeq, Long feedSeq);
    void deleteAllByUserSeq(Long userSeq);
    void deleteAllByFeedSeq(Long feedSeq);
}
