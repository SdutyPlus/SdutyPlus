package com.d205.sdutyplus.user;

import com.d205.sdutyplus.domain.user.service.UserAuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserDataManualControlTest {
    @Autowired
    UserAuthService userAuthService;

    @Test
    void deleteUser(){
        userAuthService.deleteUser();
    }

}
