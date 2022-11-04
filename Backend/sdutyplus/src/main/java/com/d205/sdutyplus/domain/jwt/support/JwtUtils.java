package com.d205.sdutyplus.domain.jwt.support;

import com.d205.sdutyplus.domain.jwt.entity.Pair;
import com.d205.sdutyplus.domain.user.entity.User;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;

import java.security.Key;
import java.util.Date;

@Log4j2
public class JwtUtils {

    public static boolean validateToken(String token) {

        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.instance) //키에 맞는 키값을 가져오는 역할
                .build()
                .parseClaimsJws(token); //키를 통해 검증,만료확인 부적절시 익셉션 발생
        return true;
    }

    /**
     * 토큰에서 seq 찾기
     *
     * @param token 토큰
     * @return seq
     */
    public static String getUserSeq(String token) {

        return Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.instance) //키에 맞는 키값을 가져오는 역할
                .build()
                .parseClaimsJws(token) //키를 통해 검증,만료확인 부적절시 익셉션 발생
                .getBody()
                .getSubject(); // username
    }

    public static String createAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject(Long.toString(user.getSeq())); // subject
        Date now = new Date(); // 현재 시간
        Pair<String, Key> key = JwtKey.getRandomKey();
        //key 이름 , key값

        // JWT Token 생성
        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst()) // keyId를 header정보로
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + JwtProperties.ACCESS_EXP_TIME)) // 토큰 만료 시간 설정
                .signWith(key.getSecond(), SignatureAlgorithm.HS512) // signature
                .compact();
    }

    //refresh token(기간만 다름)
    public static String createRefreshToken(User user) {
        Claims claims = Jwts.claims().setSubject(Long.toString(user.getSeq())); // subject
        Date now = new Date(); // 현재 시간
        Pair<String, Key> key = JwtKey.getRandomKey();
        //key 이름 , key값

        // JWT Token 생성
        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst()) // keyId를 header정보로
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + JwtProperties.REFRESH_EXP_TIME)) // 토큰 만료 시간 설정
                .signWith(key.getSecond(), SignatureAlgorithm.HS512) // signature
                .compact();
    }
}
