package com.d205.sdutyplus.domain.warn.repository;

import com.d205.sdutyplus.domain.warn.entity.WarnUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarnRepository extends JpaRepository<WarnUser, Long> {
    public boolean existsByFromUserSeqAndToUserSeq(Long fromUserSeq, Long toUserSeq);
}
