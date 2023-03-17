package com.d205.sdutyplus.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
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

    private List<String> getLastUserID(){
        return queryFactory
                .select(user.email)
                .from(user)
                .where(user.socialType.eq(SDUTY))
                .fetch();
    }
}
