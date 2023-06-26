package com.d205.sdutyplus.domain.user;

import com.d205.sdutyplus.IntegrationTest;
import com.d205.sdutyplus.domain.user.entity.SocialType;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.util.domain.user.UserUtils;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserRepositoryTest extends IntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("중복 아이디 생성, unique속성 위반 시 예외처리")
    public void UniqueViolationExceptionTest(){
        //given
        final String email = "test@sduty.com";
        final User firstUser = UserUtils.of(email, SocialType.SDUTY);
        final User secondUser = UserUtils.of(email, SocialType.SDUTY);

        //when
        final ThrowableAssert.ThrowingCallable executable = () -> {
            userRepository.save(firstUser);
            userRepository.save(secondUser);
        };

        //then
        assertThatThrownBy(executable).isInstanceOf(DataIntegrityViolationException.class);
    }
}
