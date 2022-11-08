package com.d205.sdutyplus.domain.feed.controller;

import com.d205.sdutyplus.domain.feed.dto.FeedPostDto;
import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.service.FeedService;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.d205.sdutyplus.global.response.ResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;
    
    @ApiOperation(value="게시글 등록")
    @PostMapping("")
    public ResponseEntity<?> createFeed(/*@RequestBody*/ FeedPostDto feedPostDto){
        feedService.createFeed(new Long(1), feedPostDto);
        return ResponseEntity.ok().body(ResponseDto.of(CREATE_FEED_SUCCESS));
    }

    @ApiOperation(value="전체 게시글 조회")
    @GetMapping("")
    public ResponseEntity<?> getALlFeeds(){
        List<FeedResponseDto> feedResponseDtos = feedService.getAllFeeds();
        return ResponseEntity.ok().body(ResponseDto.of(GET_ALL_FEED_SUCCESS, feedResponseDtos));
    }
    
    @ApiOperation(value = "내가 작성한 게시글 조회")
    @GetMapping("/writer")
    public ResponseEntity<?> getMyFeeds(){
        List<FeedResponseDto> feedResponseDtos = feedService.getMyFeeds(new Long(1));
        return ResponseEntity.ok().body(ResponseDto.of(GET_MY_FEED_SUCCESS, feedResponseDtos));
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping("/{feed_seq}")
    public ResponseEntity<?> deleteFeed(@PathVariable(value = "feed_seq") Long feedSeq){
        feedService.deleteFeed(feedSeq);
        return ResponseEntity.ok().body(ResponseDto.of(DELETE_FEED_SUCCESS));
    }
}
