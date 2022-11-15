package com.d205.sdutyplus.domain.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@Builder
public class FeedPostDto {
    private MultipartFile img;
    private String content;
}
