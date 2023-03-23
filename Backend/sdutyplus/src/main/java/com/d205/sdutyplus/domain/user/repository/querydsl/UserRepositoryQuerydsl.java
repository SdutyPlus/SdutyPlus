package com.d205.sdutyplus.domain.user.repository.querydsl;

import com.d205.sdutyplus.domain.warn.dto.WarnUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryQuerydsl {
    Page<WarnUserDto> findAllWarnUserPage(Pageable pageable);
    String findLastTestUserEmail();
}
