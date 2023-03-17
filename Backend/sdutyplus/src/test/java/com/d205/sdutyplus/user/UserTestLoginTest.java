package com.d205.sdutyplus.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.d205.sdutyplus.domain.user.entity.QUser.user;
import static com.d205.sdutyplus.domain.user.entity.SocialType.SDUTY;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserTestLoginTest {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Test
    public void makeIDTest(){
        //1. socialType이 SDUTY인 것 중 최근 값 불러오기, 없으면 1번 유저로 새로 만들기
        List<String> testUsers = getLastUserID();
        testUsers.add("test2@sduty.com");
        //2. parsing해서 새로운 아이디 생성하기 (ID값 = test[번호]@sduty.com 으로 변경하기, socialType = sduty)
        String newUserEmail;
        if(testUsers.size()==0){
            newUserEmail = "test1@sduty.com";
        }
        else{
            String[] prevUser = testUsers.get(testUsers.size()-1).split("@");
            StringBuilder sb = new StringBuilder();
            sb.append("test")
                    .append(Integer.parseInt(prevUser[0].substring(4))+1)
                    .append("@sduty.com");
            newUserEmail = sb.toString();
        }
        assertThat(newUserEmail).isEqualTo("test3@sduty.com");
    }

    private List<String> getLastUserID(){
        return queryFactory
                .select(user.email)
                .from(user)
                .where(user.socialType.eq(SDUTY))
                .fetch();
    }
}
