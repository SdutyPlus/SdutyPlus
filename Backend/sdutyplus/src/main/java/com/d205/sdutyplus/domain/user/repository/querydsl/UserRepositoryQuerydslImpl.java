package com.d205.sdutyplus.domain.user.repository.querydsl;

import com.d205.sdutyplus.domain.warn.dto.QWarnUserDto;
import com.d205.sdutyplus.domain.warn.dto.WarnUserDto;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.d205.sdutyplus.domain.user.entity.QUser.user;
import static com.d205.sdutyplus.domain.user.entity.SocialType.SDUTY;
import static com.d205.sdutyplus.domain.warn.entity.QWarnUser.warnUser;

@Repository
@RequiredArgsConstructor
public class UserRepositoryQuerydslImpl implements UserRepositoryQuerydsl{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<WarnUserDto> findAllWarnUserPage(Pageable pageable) {
        QueryResults<WarnUserDto> result = jpaQueryFactory
                .select(
                        new QWarnUserDto(
                                warnUser.toUser.seq,
                                warnUser.toUser.email,
                                warnUser.toUser.socialType,
                                warnUser.toUser.nickname,
                                warnUser.toUser.imgUrl,
                                warnUser.toUser.delYN,
                                warnUser.toUser.banYN
                        )
                )
                .from(warnUser)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public String findLastTestUserEmail() {
        final List<String> userEmails = jpaQueryFactory
                .select(user.email)
                .from(user)
                .where(user.socialType.eq(SDUTY))
                .fetch();
        return (userEmails.size() == 0) ? null : userEmails.get(userEmails.size()-1);
    }
}
