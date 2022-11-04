package com.d205.sdutyplus.domain.user.repository;

import com.d205.sdutyplus.domain.user.entity.SocialType;
import com.d205.sdutyplus.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBySeq(Long userSeq);
    Optional<User> findByEmailAndSocialType(String email, SocialType socialType);
}
