package com.d205.sdutyplus.domain.off.repository;

import com.d205.sdutyplus.domain.off.entity.OffFeed;
import com.d205.sdutyplus.domain.off.repository.queyrdsl.OffFeedRepositoryQuerydsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OffFeedRepository extends JpaRepository<OffFeed, Long>, OffFeedRepositoryQuerydsl {
    boolean existsByFeedSeqAndUserSeq(Long userSeq, Long feedSeq);
    void deleteAllByUserSeq(Long userSeq);
    void deleteAllByFeedSeq(Long feedSeq);
}
