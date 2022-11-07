package com.d205.sdutyplus.domain.feed.service;

import com.d205.sdutyplus.domain.feed.dto.FeedPostDto;
import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.feed.repository.FeedRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedService {

    @Value("${app.firebase-bucket}")
    private String firebaseBucket;
    private final String UPLOADURL = "feed/";
    private final FeedRepository feedRepository;

    @Transactional
    public void createFeed(Long userSeq, FeedPostDto feedPostDto){
        String imgUrl = uploadFile(feedPostDto.getImg());
        Feed feed = Feed.builder()
                .writerSeq(userSeq)
                .imgUrl(imgUrl)
                .content(feedPostDto.content).build();
        feedRepository.save(feed);
    }

    public String uploadFile(MultipartFile file){
        String originFileName = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String fileName = new MD5Generator(originFileName).toString() + "_" + uuid.toString();

        try {
            Bucket bucket = StorageClient.getInstance().bucket(firebaseBucket);
            InputStream content = new ByteArrayInputStream(file.getBytes());
            Blob blob = bucket.create(UPLOADURL+fileName, content, file.getContentType());
            return blob.getMediaLink();
        }
        catch(IOException e){
            throw new NotSupportedImageTypeException();
        }
    }
}
