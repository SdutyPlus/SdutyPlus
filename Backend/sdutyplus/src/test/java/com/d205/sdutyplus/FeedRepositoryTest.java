package com.d205.sdutyplus;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.dto.QFeedResponseDto;
import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.d205.sdutyplus.domain.feed.entity.QScrap.scrap;
import static com.d205.sdutyplus.domain.task.entity.QTask.task;
import static com.d205.sdutyplus.global.error.ErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedRepositoryTest {
    @Autowired
    private JPAQueryFactory queryFactory;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void getScrapFeed(){
        Long userSeq = 7L;
        User user = userRepository.findBySeq(userSeq)
                .orElseThrow(()->new EntityNotFoundException(USER_NOT_FOUND));

        List<FeedResponseDto> feeds = queryFactory
                .select(
                        new QFeedResponseDto(
                                scrap.feed.seq,
                                scrap.feed.writerSeq,
                                scrap.feed.imgUrl,
                                scrap.feed.content
                        )
                )
                .from(scrap)
                .where(scrap.user.eq(user))
                .fetch();

        //then
        assertThat(feeds.size()).isEqualTo(1);
    }
}
