package com.d205.sdutyplus;

import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.dto.QFeedResponseDto;
import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.feed.repository.querydsl.FeedRepositoryQuerydsl;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.JobRepository;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.entity.Job;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.d205.sdutyplus.domain.feed.entity.QScrap.scrap;
import static com.d205.sdutyplus.domain.task.entity.QTask.task;
import static com.d205.sdutyplus.global.error.ErrorCode.JOB_NOT_FOUND;
import static com.d205.sdutyplus.global.error.ErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FeedRepositoryTest {
    @Autowired
    private JPAQueryFactory queryFactory;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private FeedRepositoryQuerydsl feedRepositoryQuerydsl;

    @Test
    public void getScrapFeed(){
        Long userSeq = 7L;
        User user = userRepository.findBySeq(userSeq)
                .orElseThrow(()->new EntityNotFoundException(USER_NOT_FOUND));

        Sort.Order order = Sort.Order.desc("seq");
        Sort sort = Sort.by(order);
        Pageable pageable = PageRequest.of(0, 5, sort);

        Page<FeedResponseDto> result = feedRepositoryQuerydsl.findScrapFeedPage(user, pageable);

        for(FeedResponseDto feedResponseDto : result.getContent()){
            System.out.println("feedResponseDto.getSeq() = " + feedResponseDto.getSeq());
        }
        //then
        assertThat(result.getContent().size()).isEqualTo(1);
    }

    @Test
    public void filterFeedTest(){
        Long jobSeq = 1L;
        Job job = jobRepository.findBySeq(jobSeq)
                .orElseThrow(()->new EntityNotFoundException(JOB_NOT_FOUND));
        Sort.Order order = Sort.Order.desc("seq");
        Sort sort = Sort.by(order);
        Pageable pageable = PageRequest.of(0, 5, sort);


        Page<FeedResponseDto> result = feedRepositoryQuerydsl.findFilterFeedPage(job, pageable);

        //then
        for(FeedResponseDto feedResponseDto : result.getContent()){
            System.out.println("feedResponseDto.getSeq() = " + feedResponseDto.getSeq());
        }
        assertThat(result.getContent().size()).isEqualTo(1);
    }
}
