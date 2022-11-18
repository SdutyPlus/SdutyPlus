package com.d205.sdutyplus.domain.feed.controller;

import com.d205.sdutyplus.domain.feed.dto.FeedPostDto;
import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.dto.PagingResultDto;
import com.d205.sdutyplus.domain.feed.service.FeedService;
import com.d205.sdutyplus.global.response.ResponseCode;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static com.d205.sdutyplus.global.response.ResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed")
@Slf4j
public class FeedController {

    private final FeedService feedService;
    
    @ApiOperation(value="게시글 등록")
    @PostMapping("")
    public ResponseEntity<ResponseDto> createFeed(@ModelAttribute FeedPostDto feedPostDto){
        feedService.createFeed(feedPostDto);
        return ResponseEntity.ok().body(ResponseDto.of(CREATE_FEED_SUCCESS));
    }

    @ApiOperation(value="전체 게시글 조회")
    @GetMapping("")
    public ResponseEntity<ResponseDto> getALlFeeds(@PageableDefault Pageable pageable){
        final PagingResultDto pagingResultDto = feedService.getAllFeeds(pageable);
        return ResponseEntity.ok().body(ResponseDto.of(GET_ALL_FEED_SUCCESS, pagingResultDto));
    }

    @ApiOperation(value="게시글 상세 조회")
    @GetMapping("/{feed_seq}")
    public ResponseEntity<ResponseDto> getOneFeed(@PathVariable(value = "feed_seq") Long feedSeq){
        final FeedResponseDto feedResponseDto = feedService.getOneFeed(feedSeq);
        return ResponseEntity.ok().body(ResponseDto.of(GET_ONE_FEED_SUCCESS, feedResponseDto));
    }

    @ApiOperation(value = "내가 작성한 게시글 조회")
    @GetMapping("/writer")
    public ResponseEntity<ResponseDto> getMyFeeds(@PageableDefault Pageable pageable){
        final PagingResultDto pagingResultDto = feedService.getMyFeeds(pageable);
        return ResponseEntity.ok().body(ResponseDto.of(GET_MY_FEED_SUCCESS, pagingResultDto));
    }

    @ApiOperation(value = "내가 스크랩한 게시글 조회")
    @GetMapping("/scrap")
    public ResponseEntity<ResponseDto> getScrapFeeds(@PageableDefault Pageable pageable){
        final PagingResultDto pagingResultDto = feedService.getScrapFeeds(pageable);
        return ResponseEntity.ok().body(ResponseDto.of(GET_SCRAP_FEED_SUCCESS, pagingResultDto));
    }

    @ApiOperation(value = "직업 필터링 게시글 조회")
    @GetMapping("/filter/{job_seq}")
    public ResponseEntity<ResponseDto> getJobFilterFeeds(@PathVariable(value="job_seq") Long jobSeq, @PageableDefault Pageable pageable){
        final PagingResultDto pagingResultDto = feedService.getJobFilterFeeds(jobSeq, pageable);
        return ResponseEntity.ok().body(ResponseDto.of(GET_JOB_FILTER_FEED_SUCCESS, pagingResultDto));
    }

    @ApiOperation(value = "게시글 삭제")
    @DeleteMapping("/{feed_seq}")
    public ResponseEntity<ResponseDto> deleteFeed(@PathVariable(value = "feed_seq") Long feedSeq){
        feedService.deleteFeed(feedSeq);
        return ResponseEntity.ok().body(ResponseDto.of(DELETE_FEED_SUCCESS));
    }

    @ApiOperation(value = "게시글 스크랩")
    @PostMapping("/scrap/{feed_seq}")
    public ResponseEntity<ResponseDto> scrapFeed(@PathVariable(value = "feed_seq") Long feedSeq){
        feedService.scrapFeed(feedSeq);
        return ResponseEntity.ok().body(ResponseDto.of(UPDATE_SCRAP_FEED_SUCCESS));
    }

    @ApiOperation(value = "게시글 스크랩 취소")
    @DeleteMapping("/scrap/{feed_seq}")
    public ResponseEntity<ResponseDto> unscrapFeed(@PathVariable(value = "feed_seq") Long feedSeq){
        feedService.unscrapFeed(feedSeq);
        return ResponseEntity.ok().body(ResponseDto.of(UPDATE_UNSCRAP_FEED_SUCCESS));
    }

    @ApiOperation(value = "게시글 좋아요")
    @ApiResponses({
            @ApiResponse(code = 200, message = "F006 - 게시물 좋아요에 성공하였습니다."),
            @ApiResponse(code = 401, message = "M003 - 로그인이 필요한 화면입니다."),
    })
    @PostMapping("/like")
    public ResponseEntity<ResponseDto> likeFeed(@RequestParam Long feedSeq){
        final boolean success = feedService.likeFeed(feedSeq);
        if (success) {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.UPDATE_GOOD_FEED_SUCCESS, success));
        } else {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.UPDATE_GOOD_FEED_FAIL, success));
        }
    }

    @ApiOperation(value = "게시글 좋아요 해제")
    @ApiResponses({
            @ApiResponse(code = 200, message = "F006 - 게시물 좋아요에 성공하였습니다."),
            @ApiResponse(code = 401, message = "M003 - 로그인이 필요한 화면입니다."),
    })
    @DeleteMapping("/like")
    public ResponseEntity<ResponseDto> unlikeFeed(@RequestParam Long feedSeq){
        final boolean success = feedService.unlikeFeed(feedSeq);
        if (success) {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.UPDATE_UNGOOD_FEED_SUCCESS, success));
        } else {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.UPDATE_UNGOOD_FEED_FAIL, success));
        }
    }
}
