package com.d205.sdutyplus.domain.jwt.support;

//jwt 기본 설정
//public static final => interface 사용
public interface JwtProperties {
    //String SECRET ="{}";//secret key는 random을 쓸거임
//    int ACCESS_EXP_TIME = 3600000; //만료시간 60분
    int ACCESS_EXP_TIME = 1_209_600_000;
    int REFRESH_EXP_TIME = 1_209_600_000; //만료시간 2주
    //String TOKEN_PREFIX = "Bearer ";//전달자명
    String JWT_ACCESS_NAME = "JWT-AUTHENTICATION";
}

