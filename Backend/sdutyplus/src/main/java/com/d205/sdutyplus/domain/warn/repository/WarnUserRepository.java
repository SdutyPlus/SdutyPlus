package com.d205.sdutyplus.domain.warn.repository;

import com.d205.sdutyplus.domain.warn.entity.WarnUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarnUserRepository extends JpaRepository<WarnUser, Long> {
    boolean existsByFromUserSeqAndToUserSeq(Long fromUserSeq, Long toUserSeq);

    void deleteAllByFromUserSeq(Long userSeq);
    void deleteAllByToUserSeq(Long userSeq);
}
