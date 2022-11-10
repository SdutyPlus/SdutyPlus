package com.d205.sdutyplus.domain.user.service;


import com.d205.sdutyplus.domain.feed.repository.FeedLikeRepository;
import com.d205.sdutyplus.domain.feed.repository.FeedRepository;
import com.d205.sdutyplus.domain.feed.repository.ScrapRepository;
import com.d205.sdutyplus.domain.jwt.dto.JwtDto;
import com.d205.sdutyplus.domain.jwt.entity.Jwt;
import com.d205.sdutyplus.domain.jwt.support.JwtUtils;
import com.d205.sdutyplus.domain.jwt.repository.JwtRepository;
import com.d205.sdutyplus.domain.off.repository.OffRepository;
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

import com.d205.sdutyplus.domain.warn.repository.WarnRepository;
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

    private final JwtRepository jwtRepository;
    private final UserRepository userRepository;
    private final DailyStatisticsRepository dailyStatisticsRepository;
    private final FeedRepository feedRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final OffRepository offRepository;
    private final ScrapRepository scrapRepository;
    private final WarnRepository warnRepository;
    private final AuthUtils authUtils;
    private final SubTaskRepository subTaskRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public UserLoginDto loginUser(String email, SocialType socialType) {
        //가입된 유저인지 확인
        Optional<User> userOp = userRepository.findByEmailAndSocialType(email, socialType);
        User realUser = null;
        if(!userOp.isPresent()) {//가입안 된 user면 => DB save
            User user = new User();
            user.setEmail(email);
            user.setSocialType(socialType);
            user.setRegTime(LocalDateTime.now());
            user.setLastReport(LocalDate.now());
            realUser = userRepository.save(user);
            if(realUser == null) {
                return null;
            }
        }
        else {
            realUser = userOp.get();
        }
        JwtDto jwtDto = new JwtDto(JwtUtils.createAccessToken(realUser), JwtUtils.createRefreshToken(realUser));

        //token저장
        Jwt jwt = jwtRepository.findByUserSeq(realUser.getSeq()).orElseGet(()->new Jwt());
        jwt.setUserSeq(realUser.getSeq());
        jwt.setAccessToken(jwtDto.getAccessToken());
        jwt.setRefreshToken(jwtDto.getRefreshToken());
        jwtRepository.save(jwt);

        final long job = 0;
        UserLoginDto userLoginDto = new UserLoginDto(realUser, jwtDto, job);

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

    public boolean deleteUser(Long userSeq){
        final User user = authUtils.getLoginUser(userSeq);

        deleteUserCade(userSeq);

        return true;
    }

//    private final TaskService taskService;
    private void deleteUserCade(Long userSeq) {

        dailyStatisticsRepository.deleteByUserSeq(userSeq);
        feedRepository.deleteAllByWriterSeq(userSeq);
        feedLikeRepository.deleteAllByUserSeq(userSeq);
        jwtRepository.deleteByUserSeq(userSeq);
        offRepository.deleteAllByFromUserSeq(userSeq);
        offRepository.deleteAllByToUserSeq(userSeq);
        scrapRepository.deleteAllByUserSeq(userSeq);
        warnRepository.deleteAllByFromUserSeq(userSeq);
        warnRepository.deleteAllByToUserSeq(userSeq);

        userRepository.findBySeq(userSeq);
    }
}
