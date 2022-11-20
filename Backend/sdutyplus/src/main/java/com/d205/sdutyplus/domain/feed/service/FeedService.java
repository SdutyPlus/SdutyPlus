package com.d205.sdutyplus.domain.feed.service;

import com.d205.sdutyplus.domain.feed.dto.FeedPostDto;
import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.exception.CannotDeleteFeedException;
import com.d205.sdutyplus.global.dto.PagingResultDto;
import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.feed.entity.FeedLike;
import com.d205.sdutyplus.domain.feed.repository.FeedLikeRepository;
import com.d205.sdutyplus.domain.feed.entity.Scrap;
import com.d205.sdutyplus.domain.feed.repository.FeedRepository;
import com.d205.sdutyplus.domain.feed.repository.ScrapRepository;
import com.d205.sdutyplus.domain.off.repository.OffFeedRepository;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.JobRepository;
import com.d205.sdutyplus.domain.warn.repository.WarnFeedRepository;
import com.d205.sdutyplus.global.entity.Job;
import com.d205.sdutyplus.global.error.exception.EntityAlreadyExistException;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.global.error.exception.NotSupportedImageTypeException;
import com.d205.sdutyplus.util.AuthUtils;
import com.d205.sdutyplus.util.MD5Generator;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static com.d205.sdutyplus.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {

    @Value("${app.firebase-bucket}")
    private String firebaseBucket;
    private final String UPLOADURL = "feed/";
    private final AuthUtils authUtils;
    private final FeedRepository feedRepository;
    private final ScrapRepository scrapRepository;
    private final FeedLikeRepository feedLikeRepository;
    private final JobRepository jobRepository;
    private final WarnFeedRepository warnFeedRepository;
    private final OffFeedRepository offFeedRepository;


    @Transactional
    public void createFeed(FeedPostDto feedPostDto){
        final User user = authUtils.getLoginUser();
        final String imgUrl = uploadFile(feedPostDto.getImg());
        final Feed feed = Feed.builder()
                .writer(user)
                .imgUrl(imgUrl)
                .content(feedPostDto.getContent()).build();
        feedRepository.save(feed);
    }

    public PagingResultDto getAllFeeds(Pageable pageable){
        final Long userSeq = authUtils.getLoginUserSeq();

        final Page<FeedResponseDto> allFeeds = feedRepository.findAllFeedPage(userSeq, pageable);
        final PagingResultDto<FeedResponseDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), allFeeds.getTotalPages() - 1, allFeeds.getContent());
        return pagingResultDto;
    }

    public FeedResponseDto getOneFeed(Long feedSeq){
        final Long userSeq = authUtils.getLoginUserSeq();

        final FeedResponseDto feedResponseDto = feedRepository.findFeedBySeq(userSeq, feedSeq)
                .orElseThrow(()->new EntityNotFoundException(FEED_NOT_FOUND));
        return feedResponseDto;
    }

    public PagingResultDto getMyFeeds(Pageable pageable){
        final Long writerSeq = authUtils.getLoginUserSeq();

        final Page<FeedResponseDto> myfeeds = feedRepository.findMyFeedPage(writerSeq, pageable);
        final PagingResultDto<FeedResponseDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), myfeeds.getTotalPages() - 1, myfeeds.getContent());
        return pagingResultDto;
    }

    public PagingResultDto getScrapFeeds(Pageable pageable){
        final User user = authUtils.getLoginUser();

        final Page<FeedResponseDto> feedPage = feedRepository.findScrapFeedPage(user, pageable);
        final PagingResultDto<FeedResponseDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), feedPage.getTotalPages() - 1, feedPage.getContent());
        return pagingResultDto;
    }

    public PagingResultDto getJobFilterFeeds(Long jobSeq, Pageable pageable){
        final Long userSeq = authUtils.getLoginUserSeq();

        final Job job = jobRepository.findBySeq(jobSeq).orElseThrow(
                ()->new EntityNotFoundException(JOB_NOT_FOUND)
        );
        final Page<FeedResponseDto> feedPage = feedRepository.findFilterFeedPage(userSeq, job, pageable);
        final PagingResultDto<FeedResponseDto> pagingResultDto = new PagingResultDto(pageable.getPageNumber(), feedPage.getTotalPages() - 1, feedPage.getContent());
        return pagingResultDto;
    }

    @Transactional
    public void deleteFeed(Long feedSeq){
        final Long userSeq = authUtils.getLoginUserSeq();
        final Feed feed = getFeed(feedSeq);
        
        if(!feed.getWriter().getSeq().equals(userSeq)){
            throw new CannotDeleteFeedException();
        }

        scrapRepository.deleteAllByFeedSeq(feedSeq);
        feedLikeRepository.deleteAllByFeedSeq(feedSeq);
        warnFeedRepository.deleteAllByFeedSeq(feedSeq);
        offFeedRepository.deleteAllByFeedSeq(feedSeq);

        removeFile(feed.getImgUrl());
        feedRepository.delete(feed);
    }

    @Transactional
    public void deleteAllFeedByUserSeq(Long userSeq){
        final List<Feed> feeds = feedRepository.findAllByWriterSeq(userSeq);
        for(Feed feed : feeds){
            scrapRepository.deleteAllByFeedSeq(feed.getSeq());
            feedLikeRepository.deleteAllByFeedSeq(feed.getSeq());
            warnFeedRepository.deleteAllByFeedSeq(feed.getSeq());
            offFeedRepository.deleteAllByFeedSeq(feed.getSeq());

            removeFile(feed.getImgUrl());
            feedRepository.delete(feed);
        }
    }

    @Transactional
    public void scrapFeed(Long feedSeq){
        final User user = authUtils.getLoginUser();
        final Feed feed = getFeed(feedSeq);

        if(scrapRepository.existsByUserSeqAndFeedSeq(user.getSeq(), feedSeq)){
            throw new EntityAlreadyExistException(FEED_SCRAP_ALREADY_EXIST);
        }

        scrapRepository.save(new Scrap(user, feed));
    }

    @Transactional
    public void unscrapFeed(Long feedSeq){
        final User user = authUtils.getLoginUser();
        
        final Scrap scrap = scrapRepository.findByUserSeqAndFeedSeq(user.getSeq(), feedSeq)
                .orElseThrow(()->new EntityNotFoundException(FEED_SCRAP_NOT_FOUND));
        scrapRepository.delete(scrap);
    }

    @Transactional
    public void deleteAllFeedScrapByUserSeq(Long userSeq){
        scrapRepository.deleteAllByUserSeq(userSeq);
    }

    @Transactional
    public boolean likeFeed(Long feedSeq) {
        final User user = authUtils.getLoginUser();
        final Feed feed = getFeed(feedSeq);

        if (feedLikeRepository.existsByUserSeqAndFeedSeq(user.getSeq(), feedSeq)){
            throw new EntityAlreadyExistException(FEED_LIKE_ALREADY_EXIST);
        }

        feedLikeRepository.save(new FeedLike(user, feed));
        return true;
    }

    @Transactional
    public boolean unlikeFeed(Long feedSeq) {
        final User user = authUtils.getLoginUser();
        final Feed feed = getFeed(feedSeq);

        FeedLike feedLike = feedLikeRepository.findByUserAndFeed(user, feed)
                .orElseThrow(() -> new EntityNotFoundException(FEED_LIKE_NOT_FOUND));

        feedLikeRepository.delete(feedLike);
        return true;
    }

    @Transactional
    public void deleteAllFeedLikeByUserSeq(Long userSeq){
        feedLikeRepository.deleteAllByUserSeq(userSeq);
    }

    //get & set => private
    private String uploadFile(MultipartFile file){
        final String originFileName = file.getOriginalFilename();
        final UUID uuid = UUID.randomUUID();
        final String fileName = new MD5Generator(originFileName).toString() + "_" + uuid.toString();

        try {
            final Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
            final InputStream content = new ByteArrayInputStream(file.getBytes());
            final Blob blob = bucket.create(UPLOADURL+fileName, content, file.getContentType());
            return "https://firebasestorage.googleapis.com/v0/b/"+blob.getBucket()+"/o/"+blob.getName().replace(UPLOADURL, "feed%2F")+"?alt=media";
        }
        catch(IOException e){
            throw new NotSupportedImageTypeException();
        }
    }

    private void removeFile(String imgUrl){
        final Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        final String temp[] = imgUrl.split("/o/");
        final String fileName = temp[1].replace("%2F", "/").replace("?alt=media", "");
        bucket.get(fileName).delete();
    }

    private Feed getFeed(Long seq){
        return feedRepository.findById(seq)
                .orElseThrow(()->new EntityNotFoundException(FEED_NOT_FOUND));
    }
}
