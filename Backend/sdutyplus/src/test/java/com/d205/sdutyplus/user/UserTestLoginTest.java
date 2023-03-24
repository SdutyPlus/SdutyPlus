package com.d205.sdutyplus.user;

import com.d205.sdutyplus.domain.user.controller.UserAuthController;
import com.d205.sdutyplus.domain.user.dto.UserLoginDto;
import com.d205.sdutyplus.domain.user.service.UserAuthService;
import com.d205.sdutyplus.global.response.ResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.d205.sdutyplus.domain.user.entity.QUser.user;
import static com.d205.sdutyplus.domain.user.entity.SocialType.SDUTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserTestLoginTest {

    @Autowired
    private JPAQueryFactory queryFactory;
    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private UserAuthController userAuthController;

    @Test
    @Transactional
    @DisplayName("테스트용 계정 생성 테스트")
    public void makeTestIDTest(){
        List<String> testUsers = getLastUserID();
        String testAccount = "test2@sduty.com";
        String newUserEmail;

        if(testUsers.size()==0){
            newUserEmail = "test1@sduty.com";
            assertThat(newUserEmail).isEqualTo("test1@sduty.com");
        }
        else{
            testUsers.add(testAccount);
            String[] prevUser = testUsers.get(testUsers.size()-1).split("@");
            StringBuilder sb = new StringBuilder();
            sb.append("test")
                    .append(Integer.parseInt(prevUser[0].substring(4))+1)
                    .append("@sduty.com");
            newUserEmail = sb.toString();
            assertThat(newUserEmail).isEqualTo("test3@sduty.com");
        }

    }
    
//    @Test
//    @DisplayName("테스트 로그인 중복 요청 테스트")
//    public void doubleRequestSend() throws InterruptedException {
//        int numberOfThreads = 30;
//        Set<String> set = new HashSet<>();
//        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
//        CountDownLatch latch = new CountDownLatch(numberOfThreads);
//        for(int i=0; i<numberOfThreads; i++){
//            service.execute(()->{
//                ResponseEntity<ResponseDto> response = userAuthController.testLogin();
//                UserLoginDto userLoginDto = (UserLoginDto)response.getBody().getData();
//                set.add(userLoginDto.getEmail());
//                latch.countDown();
//            });
//        }
//        latch.await();
//        assertEquals(numberOfThreads, set.size());
//    }


//    @Test
//    public void deleteTestUser(){
//        List<Long> testUsers = queryFactory
//                .select(user.seq)
//                .from(user)
//                .where(user.socialType.eq(SDUTY))
//                .fetch();
//
//        for(Long seq : testUsers){
//            userAuthService.deleteUser(seq);
//        }
//    }

    private List<String> getLastUserID(){
        return queryFactory
                .select(user.email)
                .from(user)
                .where(user.socialType.eq(SDUTY))
                .fetch();
    }
}
