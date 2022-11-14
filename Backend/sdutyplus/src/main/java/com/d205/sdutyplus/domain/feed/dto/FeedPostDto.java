package com.d205.sdutyplus.domain.feed.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FeedPostDto {
    private MultipartFile img;
    private String content;
}
