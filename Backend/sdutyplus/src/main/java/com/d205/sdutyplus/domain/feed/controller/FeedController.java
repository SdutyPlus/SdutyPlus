package com.d205.sdutyplus.domain.feed.controller;

import com.d205.sdutyplus.domain.feed.dto.FeedPostDto;
import com.d205.sdutyplus.domain.feed.service.FeedService;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.d205.sdutyplus.global.response.ResponseCode.CREATE_FEED_SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;
    
    @ApiOperation(value="피드 등록")
    @PostMapping("")
    public ResponseEntity<?> createFeed(/*@RequestBody*/ FeedPostDto feedPostDto){
        feedService.createFeed(new Long(1), feedPostDto);
        return ResponseEntity.ok().body(ResponseDto.of(CREATE_FEED_SUCCESS));
    }
}
