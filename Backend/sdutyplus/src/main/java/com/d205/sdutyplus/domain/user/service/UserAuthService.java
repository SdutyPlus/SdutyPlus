package com.d205.sdutyplus.domain.user.service;

import com.d205.sdutyplus.domain.feed.service.FeedService;
import com.d205.sdutyplus.domain.jwt.dto.JwtDto;
import com.d205.sdutyplus.domain.jwt.entity.Jwt;
import com.d205.sdutyplus.domain.jwt.support.JwtUtils;
import com.d205.sdutyplus.domain.jwt.repository.JwtRepository;
import com.d205.sdutyplus.domain.off.repository.OffUserRepository;
import com.d205.sdutyplus.domain.off.service.OffService;
import com.d205.sdutyplus.domain.statistics.entity.DailyStatistics;
import com.d205.sdutyplus.domain.statistics.repository.DailyStatisticsRepository;
import com.d205.sdutyplus.domain.user.dto.UserLoginDto;
import com.d205.sdutyplus.domain.user.entity.SocialType;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.d205.sdutyplus.domain.warn.repository.WarnUserRepository;
import com.d205.sdutyplus.domain.warn.service.WarnService;
import com.d205.sdutyplus.global.error.exception.EntityAlreadyExistException;
import com.d205.sdutyplus.util.AuthUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

import static com.d205.sdutyplus.global.error.ErrorCode.USER_ALREADY_EXIST;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAuthService {

    private final FeedService feedService;
    private final WarnService warnService;
    private final OffService offService;
    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final DailyStatisticsRepository dailyStatisticsRepository;
    private final OffUserRepository offUserRepository;
    private final WarnUserRepository warnUserRepository;
    private final AuthUtils authUtils;

    @Transactional
    public UserLoginDto loginUser(String email, SocialType socialType) {
        final Optional<User> registedUser = userRepository.findByEmailAndSocialType(email, socialType);
        final User user = registedUser.orElseGet(() -> registUser(email, socialType));

        final JwtDto jwtDto = issueJWT(user);

        return new UserLoginDto(user, jwtDto, "");
    }

    @Transactional
    public UserLoginDto loginTestUser(){
        final StringBuilder sb = new StringBuilder();
        sb.append(RandomStringUtils.random(6, true, false))
                .append((int)(Math.random()*99) + 1)
                .append("@sduty.com");

        final String email = sb.toString();
        if(userRepository.existsByEmailAndSocialType(email, SocialType.SDUTY)){
            throw new EntityAlreadyExistException(USER_ALREADY_EXIST);
        }

        final User user = registUser(email, SocialType.SDUTY);
        final JwtDto jwtDto = issueJWT(user);

        return new UserLoginDto(user, jwtDto, "");
    }


    public Map<String, Object> getNaverUserInfo(String token) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> header = new HttpEntity<>(headers);
        ResponseEntity<String> res = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                header,
                String.class
        );

        //결과 parsing
        Map<String, Object> userInfo = null;
        JSONParser jsonParser = new JSONParser();
        try {
            System.out.println(res.getBody());
            JSONObject jsonObj = (JSONObject)jsonParser.parse(res.getBody());
            jsonObj = (JSONObject)jsonParser.parse(jsonObj.get("response").toString());
            userInfo = new HashMap<>();
            userInfo.put("email", jsonObj.get("email"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    //kakao 회원정보 받기
    public Map<String, Object> getKakaoUserInfo(String token) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> header = new HttpEntity<>(headers);
        ResponseEntity<String> res = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                header,
                String.class
                );

        //결과 parsing
        Map<String, Object> userInfo = null;
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObj = (JSONObject)jsonParser.parse(res.getBody());
            jsonObj = (JSONObject)jsonParser.parse(jsonObj.get("kakao_account").toString());
            userInfo = new HashMap<>();
            userInfo.put("email", jsonObj.get("email"));

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    @Transactional
    public boolean deleteUser(){
        final User user = authUtils.getLoginUser();

        deleteUserCade(user.getSeq());

        return true;
    }

    public boolean checkTokenExpiration(){
        final Long userSeq = authUtils.getLoginUserSeq();

        return true;
    }

    private User registUser(String email, SocialType socialType){
        User user = new User();
        user.setEmail(email);
        user.setSocialType(socialType);
        user.setRegTime(LocalDateTime.now());
        user.setLastReport(LocalDate.now());
        userRepository.save(user);

        final DailyStatistics dailyStatistics = createUserStatisticsInfo(user);
        dailyStatisticsRepository.save(dailyStatistics);
        return user;
    }

    private JwtDto issueJWT(User user){
        final Jwt jwt = jwtRepository.findByUserSeq(user.getSeq()).orElseGet(()->new Jwt());
        jwt.setUserSeq(user.getSeq());
        jwt.setAccessToken(JwtUtils.createAccessToken(user));
        jwt.setRefreshToken(JwtUtils.createRefreshToken(user));
        jwtRepository.save(jwt);
        return new JwtDto(jwt.getAccessToken(), jwt.getRefreshToken());
    }

    private void deleteUserCade(Long userSeq) {

        dailyStatisticsRepository.deleteByUserSeq(userSeq);

        feedService.deleteAllFeedScrapByUserSeq(userSeq);
        feedService.deleteAllFeedLikeByUserSeq(userSeq);
        warnService.deleteAllFeedWarnByUserSeq(userSeq);
        offService.deleteAllFeedOffByUserSeq(userSeq);

        feedService.deleteAllFeedByUserSeq(userSeq);

        warnUserRepository.deleteAllByFromUserSeq(userSeq);
        warnUserRepository.deleteAllByToUserSeq(userSeq);
        jwtRepository.deleteByUserSeq(userSeq);
        offUserRepository.deleteAllByFromUserSeq(userSeq);
        offUserRepository.deleteAllByToUserSeq(userSeq);

        userRepository.deleteById(userSeq);
    }

    private DailyStatistics createUserStatisticsInfo(User user){
        DailyStatistics result = new DailyStatistics();
        result.setUserSeq(user.getSeq());

        return result;
    }

}
