package com.d205.sdutyplus.domain.feed.service;

import com.d205.sdutyplus.domain.feed.dto.FeedPostDto;
import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.feed.repository.FeedRepository;
import com.d205.sdutyplus.domain.feed.repository.querydsl.FeedRepositoryQuerydsl;
import com.d205.sdutyplus.global.error.exception.EntityNotFoundException;
import com.d205.sdutyplus.global.error.exception.NotSupportedImageTypeException;
import com.d205.sdutyplus.util.MD5Generator;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static com.d205.sdutyplus.global.error.ErrorCode.FEED_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class FeedService {

    @Value("${app.firebase-bucket}")
    private String firebaseBucket;
    private final String UPLOADURL = "feed/";
    private final FeedRepository feedRepository;
    private final FeedRepositoryQuerydsl feedRepositoryQuerydsl;

    @Transactional
    public void createFeed(Long userSeq, FeedPostDto feedPostDto){
        String imgUrl = uploadFile(feedPostDto.getImg());
        Feed feed = Feed.builder()
                .writerSeq(userSeq)
                .imgUrl(imgUrl)
                .content(feedPostDto.content).build();
        feedRepository.save(feed);
    }

    public List<FeedResponseDto> getAllFeeds(){
        return feedRepositoryQuerydsl.findAllFeeds();
    }

    public List<FeedResponseDto> getMyFeeds(Long writerSeq){
        return feedRepositoryQuerydsl.findMyFeeds(writerSeq);
    }

    @Transactional
    public void deleteFeed(Long seq){
        Feed feed = getFeed(seq);
        //TODO : firebase에 업로드된 파일 삭제
        removeFile(feed.getImgUrl());
        feedRepository.delete(feed);
    }

    public String uploadFile(MultipartFile file){
        String originFileName = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String fileName = new MD5Generator(originFileName).toString() + "_" + uuid.toString();

        try {
            Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
            InputStream content = new ByteArrayInputStream(file.getBytes());
            Blob blob = bucket.create(UPLOADURL+fileName, content, file.getContentType());
            return "https://firebasestorage.googleapis.com/v0/b/"+blob.getBucket()+"/o/"+blob.getName().replace(UPLOADURL, "feed%2F")+"?alt=media";
        }
        catch(IOException e){
            throw new NotSupportedImageTypeException();
        }
    }

    public void removeFile(String imgUrl){
        Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
        String temp[] = imgUrl.split("/o/");
        String fileName = temp[1].replace("%2F", "/").replace("?alt=media", "");
        bucket.get(fileName).delete();
    }

    //get & set => private
    private Feed getFeed(Long seq){
        return feedRepository.findById(seq)
                .orElseThrow(()->new EntityNotFoundException(FEED_NOT_FOUND));
    }
}
