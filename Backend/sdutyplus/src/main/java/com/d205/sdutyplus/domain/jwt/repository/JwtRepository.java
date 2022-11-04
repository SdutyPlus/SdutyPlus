package com.d205.sdutyplus.domain.jwt.repository;

import com.d205.sdutyplus.domain.jwt.entity.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtRepository extends JpaRepository<Jwt, Long> {

    Optional<Jwt> findByUserSeq(Long userSeq);

    Optional<Jwt> findByRefreshToken(String refreshToken);

    void deleteByUserSeq(Long userSeq);

}
