package com.d205.sdutyplus.domain.user;

import com.d205.sdutyplus.IntegrationTest;
import com.d205.sdutyplus.domain.user.dto.UserLoginDto;
import com.d205.sdutyplus.domain.user.service.UserAuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTestLoginTest extends IntegrationTest {
    @Autowired
    private UserAuthService userAuthService;

    @Test
    @DisplayName("테스트용 계정 생성 테스트")
    public void makeTestIDTest() {
        UserLoginDto userLoginDto = userAuthService.loginTestUser();
        assertThat(userLoginDto.getEmail().endsWith("@sduty.com"));
    }
}
