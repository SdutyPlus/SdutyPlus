package com.d205.sdutyplus.domain.feed.service;

import com.d205.sdutyplus.domain.feed.dto.FeedPostDto;
import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.dto.PagingResultDto;
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
import java.util.UUID;

import static com.d205.sdutyplus.global.error.ErrorCode.*;

@Slf4j
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
    public void createFeed(Long userSeq, FeedPostDto feedPostDto){
        final User user = authUtils.getLoginUser(userSeq);
        final String imgUrl = uploadFile(feedPostDto.getImg());
        final Feed feed = Feed.builder()
                .writer(user)
                .imgUrl(imgUrl)
                .content(feedPostDto.getContent()).build();
        feedRepository.save(feed);
    }

    public PagingResultDto getAllFeeds(Long userSeq, Pageable pageable){
        final Page<FeedResponseDto> allFeeds = feedRepository.findAllFeeds(userSeq, pageable);
        final PagingResultDto pagingResultDto = new PagingResultDto<FeedResponseDto>(pageable.getPageNumber(), allFeeds.getTotalPages() - 1, allFeeds.getContent());
        return pagingResultDto;
    }

    public FeedResponseDto getOneFeed(Long feedSeq){
        final FeedResponseDto feedResponseDto = feedRepository.findFeedBySeq(feedSeq)
                .orElseThrow(()->new EntityNotFoundException(FEED_NOT_FOUND));
        return feedResponseDto;
    }

    public PagingResultDto getMyFeeds(Long writerSeq, Pageable pageable){
        final Page<FeedResponseDto> myfeeds = feedRepository.findMyFeedPage(writerSeq, pageable);
        final PagingResultDto pagingResultDto = new PagingResultDto<FeedResponseDto>(pageable.getPageNumber(), myfeeds.getTotalPages() - 1, myfeeds.getContent());
        return pagingResultDto;
    }

    public PagingResultDto getScrapFeeds(Long userSeq, Pageable pageable){
        final User user = authUtils.getLoginUser(userSeq);
        final Page<FeedResponseDto> feedPage = feedRepository.findScrapFeedPage(user, pageable);
        final PagingResultDto pagingResultDto = new PagingResultDto<FeedResponseDto>(pageable.getPageNumber(), feedPage.getTotalPages() - 1, feedPage.getContent());
        return pagingResultDto;
    }

    public PagingResultDto getJobFilterFeeds(Long userSeq, Long jobSeq, Pageable pageable){
        final Job job = jobRepository.findBySeq(jobSeq).orElseThrow(
                ()->new EntityNotFoundException(JOB_NOT_FOUND)
        );
        final Page<FeedResponseDto> feedPage = feedRepository.findFilterFeedPage(userSeq, job, pageable);
        final PagingResultDto pagingResultDto = new PagingResultDto<FeedResponseDto>(pageable.getPageNumber(), feedPage.getTotalPages() - 1, feedPage.getContent());
        return pagingResultDto;
    }

    @Transactional
    public void deleteFeed(Long seq){
        final Feed feed = getFeed(seq);

        scrapRepository.deleteAllByFeedSeq(seq);
        feedLikeRepository.deleteAllByFeedSeq(seq);
        warnFeedRepository.deleteAllByFeedSeq(seq);
        offFeedRepository.deleteAllByFeedSeq(seq);
        
        removeFile(feed.getImgUrl());
        feedRepository.delete(feed);
    }

    @Transactional
    public void scrapFeed(Long userSeq, Long feedSeq){
        final Feed feed = getFeed(feedSeq);
        final User user = authUtils.getLoginUser(userSeq);

        if(scrapRepository.findByUserSeqAndFeedSeq(userSeq, feedSeq).isPresent()){
            throw new EntityAlreadyExistException(FEED_SCRAP_ALREADY_EXIST);
        }

        scrapRepository.save(new Scrap(user, feed));
    }

    @Transactional
    public void unscrapFeed(Long userSeq, Long feedSeq){
        final Feed feed = getFeed(feedSeq);
        final User user = authUtils.getLoginUser(userSeq);

        final Scrap scrap = scrapRepository.findByUserSeqAndFeedSeq(userSeq, feedSeq)
                .orElseThrow(()->new EntityNotFoundException(FEED_SCRAP_NOT_FOUND));
        scrapRepository.delete(scrap);
    }

    @Transactional
    public boolean likeFeed(Long userSeq, Long feedSeq) {
        final Feed feed = getFeed(feedSeq);
        final User user = authUtils.getLoginUser(userSeq);

        if (feedLikeRepository.findByUserAndFeed(user, feed).isPresent()){
            throw new EntityAlreadyExistException(FEED_LIKE_ALREADY_EXIST);
        }

        feedLikeRepository.save(new FeedLike(user, feed));
        return true;
    }

    @Transactional
    public boolean unlikeFeed(Long userSeq, Long feedSeq) {
        final Feed feed = getFeed(feedSeq);
        final User user = authUtils.getLoginUser(userSeq);

        FeedLike feedLike = feedLikeRepository.findByUserAndFeed(user, feed)
                .orElseThrow(() -> new EntityNotFoundException(FEED_LIKE_NOT_FOUND));

        feedLikeRepository.delete(feedLike);
        return true;
    }

    //get & set => private
    private String uploadFile(MultipartFile file){
        log.debug("업로드할 파일 : "+file);
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
