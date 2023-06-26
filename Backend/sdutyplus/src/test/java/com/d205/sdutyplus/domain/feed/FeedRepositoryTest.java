package com.d205.sdutyplus.domain.feed;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.repository.FeedRepository;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.util.domain.feed.FeedUtils;
import com.d205.sdutyplus.util.domain.user.UserUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class FeedRepositoryTest {
    @Autowired
    private FeedRepository feedRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void init(){
        final User user = userRepository.findById(1L).orElseGet(()->{
            User newUser = UserUtils.newInstance();
            newUser.setSeq(1L);
            return userRepository.save(newUser);
        });

        final int MINCOUNT = 5;
        for(int i=0; i<MINCOUNT; i++){
            feedRepository.save(FeedUtils.newInstance(user));
        }
    }

    @Test
    @DisplayName("페이지가 누락되면 안 된다!")
    public void findFeedsMissingTest() {
        //given
        Long userSeq = 1L;
        Pageable page1 = PageRequest.of(0, 2);
        Pageable page2 = PageRequest.of(1, 2);

        //when
        List<FeedResponseDto> firstPage = feedRepository.findAllFeedPage(userSeq, page1).getContent();
        List<FeedResponseDto> secondPage = feedRepository.findAllFeedPage(userSeq, page2).getContent();

        FeedResponseDto beforeFirstFeed = secondPage.get(0);
        feedRepository.deleteById(firstPage.get(0).getSeq());
        secondPage = feedRepository.findAllFeedPage(userSeq, page2).getContent();
        FeedResponseDto afterFirstFeed = secondPage.get(0);

        //then
        assertThat(beforeFirstFeed.getSeq()).isEqualTo(afterFirstFeed.getSeq());
    }

}
