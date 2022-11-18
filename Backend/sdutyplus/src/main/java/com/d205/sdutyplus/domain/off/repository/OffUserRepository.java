package com.d205.sdutyplus.domain.off.repository;

import com.d205.sdutyplus.domain.off.entity.OffUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OffUserRepository extends JpaRepository<OffUser, Long> {
    boolean existsByFromUserSeqAndToUserSeq(Long fromUserSeq, Long toUserSeq);

    void deleteAllByFromUserSeq(Long userSeq);
    void deleteAllByToUserSeq(Long userSeq);
}
