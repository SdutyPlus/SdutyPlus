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
import com.d205.sdutyplus.domain.feed.repository.querydsl.FeedRepositoryQuerydsl;
import com.d205.sdutyplus.domain.user.entity.User;
import com.d205.sdutyplus.domain.user.repository.UserRepository;
import com.d205.sdutyplus.global.error.exception.EntityAlreadyExistException;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.global.error.exception.NotSupportedImageTypeException;
import com.d205.sdutyplus.util.AuthUtils;
import com.d205.sdutyplus.util.MD5Generator;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static com.d205.sdutyplus.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class FeedService {

    @Value("${app.firebase-bucket}")
    private String firebaseBucket;
    private final String UPLOADURL = "feed/";
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final FeedRepositoryQuerydsl feedRepositoryQuerydsl;
    private final FeedLikeRepository feedLikeRepository;
    private final AuthUtils authUtils;

    @Transactional
    public void createFeed(Long userSeq, FeedPostDto feedPostDto){
        final String imgUrl = uploadFile(feedPostDto.getImg());
        final Feed feed = Feed.builder()
                .writerSeq(userSeq)
                .imgUrl(imgUrl)
                .content(feedPostDto.content).build();
        feedRepository.save(feed);
    }

    public List<FeedResponseDto> getAllFeeds(){
        return feedRepositoryQuerydsl.findAllFeeds();
    }

    public PagingResultDto getMyFeeds(Long writerSeq, Pageable pageable){
        final Page<FeedResponseDto> myfeeds = feedRepositoryQuerydsl.findMyFeedPage(writerSeq, pageable);
        final PagingResultDto pagingResultDto = new PagingResultDto<FeedResponseDto>(pageable.getPageNumber(), myfeeds.getTotalPages() - 1, myfeeds.getContent());
        return pagingResultDto;
    }

    public PagingResultDto getScrapFeeds(Long userSeq, Pageable pageable){
        final User user = authUtils.getLoginUser(userSeq);
        final Page<FeedResponseDto> feedPage = feedRepositoryQuerydsl.findScrapFeedPage(user, pageable);
        final PagingResultDto pagingResultDto = new PagingResultDto<FeedResponseDto>(pageable.getPageNumber(), feedPage.getTotalPages() - 1, feedPage.getContent());
        return pagingResultDto;
    }

    @Transactional
    public void deleteFeed(Long seq){
        final Feed feed = getFeed(seq);
        //TODO : firebase에 업로드된 파일 삭제
        removeFile(feed.getImgUrl());
        feedRepository.delete(feed);
    }

    @Transactional
    public void scrapFeed(Long userSeq, Long feedSeq){
        final Feed feed = getFeed(feedSeq);
        final User user = authUtils.getLoginUser(userSeq);

        scrapRepository.save(Scrap.builder()
                .user(user)
                .feed(feed)
                .build());
    }

    @Transactional
    public void unscrapFeed(Long userSeq, Long feedSeq){
        final Feed feed = getFeed(feedSeq);
        final User user = authUtils.getLoginUser(userSeq);

        final Scrap scrap = scrapRepository.findByUserAndFeed(user, feed)
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
