package com.d205.sdutyplus.domain.user.service;


import com.d205.sdutyplus.domain.feed.repository.FeedLikeRepository;
import com.d205.sdutyplus.domain.feed.repository.FeedRepository;
import com.d205.sdutyplus.domain.feed.repository.ScrapRepository;
import com.d205.sdutyplus.domain.feed.repository.querydsl.FeedRepositoryQuerydsl;
import com.d205.sdutyplus.domain.feed.service.FeedService;
import com.d205.sdutyplus.domain.jwt.dto.JwtDto;
import com.d205.sdutyplus.domain.jwt.entity.Jwt;
import com.d205.sdutyplus.domain.jwt.support.JwtUtils;
import com.d205.sdutyplus.domain.jwt.repository.JwtRepository;
import com.d205.sdutyplus.domain.off.repository.OffFeedRepository;
import com.d205.sdutyplus.domain.off.repository.OffUserRepository;
import com.d205.sdutyplus.domain.off.repository.queyrdsl.OffFeedRepositoryQuerydsl;
import com.d205.sdutyplus.domain.off.service.OffService;
import com.d205.sdutyplus.domain.statistics.entity.DailyStatistics;
import com.d205.sdutyplus.domain.statistics.repository.DailyStatisticsRepository;
import com.d205.sdutyplus.domain.task.repository.SubTaskRepository;
import com.d205.sdutyplus.domain.task.repository.TaskRepository;
import com.d205.sdutyplus.domain.user.dto.UserLoginDto;
import com.d205.sdutyplus.domain.user.entity.SocialType;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import com.d205.sdutyplus.domain.warn.repository.WarnFeedRepository;
import com.d205.sdutyplus.domain.warn.repository.WarnUserRepository;
import com.d205.sdutyplus.domain.warn.repository.querydsl.WarnFeedRepositoryQuerydsl;
import com.d205.sdutyplus.domain.warn.service.WarnService;
import com.d205.sdutyplus.util.AuthUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final FeedService feedService;
    private final WarnService warnService;
    private final OffService offService;
    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final DailyStatisticsRepository dailyStatisticsRepository;
//    private final FeedRepository feedRepository;
//    private final FeedLikeRepository feedLikeRepository;
    private final OffUserRepository offUserRepository;
//    private final OffFeedRepository offFeedRepository;
//    private final ScrapRepository scrapRepository;
    private final WarnUserRepository warnUserRepository;
//    private final WarnFeedRepository warnFeedRepository;
    private final AuthUtils authUtils;

    @Transactional
    public UserLoginDto loginUser(String email, SocialType socialType) {
        //가입된 유저인지 확인
        final Optional<User> userOp = userRepository.findByEmailAndSocialType(email, socialType);
        User realUser = null;
        if(!userOp.isPresent()) {//가입안 된 user면 => DB save
            User user = new User();
            user.setEmail(email);
            user.setSocialType(socialType);
            user.setRegTime(LocalDateTime.now());
            user.setLastReport(LocalDate.now());
            realUser = userRepository.save(user);

            final DailyStatistics dailyStatistics = createUserStatisticsInfo(user);
            dailyStatisticsRepository.save(dailyStatistics);

            if(realUser == null) {
                return null;
            }
        }
        else {
            realUser = userOp.get();
        }
        final JwtDto jwtDto = new JwtDto(JwtUtils.createAccessToken(realUser), JwtUtils.createRefreshToken(realUser));

        //token저장
        final Jwt jwt = jwtRepository.findByUserSeq(realUser.getSeq()).orElseGet(()->new Jwt());
        jwt.setUserSeq(realUser.getSeq());
        jwt.setAccessToken(jwtDto.getAccessToken());
        jwt.setRefreshToken(jwtDto.getRefreshToken());
        jwtRepository.save(jwt);

        final String jobName = "";
        final UserLoginDto userLoginDto = new UserLoginDto(realUser, jwtDto, jobName);

        return userLoginDto;
    }


    //naver 회원정보 받기
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
        final Long userSeq = authUtils.getLoginUserSeq();
        final User user = authUtils.getLoginUser(userSeq);

        deleteUserCade(userSeq);

        return true;
    }

    @Transactional
    public boolean checkTokenExpiration(){
        final Long userSeq = authUtils.getLoginUserSeq();

        return true;
    }

    @Transactional
    public void deleteUserCade(Long userSeq) {

        dailyStatisticsRepository.deleteByUserSeq(userSeq);
        //스크랩, 좋아요, 신고, 차단 정보 삭제
        feedService.deleteAllFeedScrapByUserSeq(userSeq);
        feedService.deleteAllFeedLikeByUserSeq(userSeq);
        warnService.deleteAllFeedWarnByUserSeq(userSeq);
        offService.deleteAllFeedOffByUserSeq(userSeq);
        //내가 쓴 글 삭제
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
