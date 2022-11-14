package com.d205.sdutyplus.domain.feed.controller;

import com.d205.sdutyplus.domain.feed.dto.FeedPostDto;
import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.dto.PagingResultDto;
import com.d205.sdutyplus.domain.feed.service.FeedService;
import com.d205.sdutyplus.global.response.ResponseCode;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

import static com.d205.sdutyplus.global.response.ResponseCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/feed")
public class FeedController {

    private final FeedService feedService;
    
    @ApiOperation(value="게시글 등록")
    @PostMapping("")
    public ResponseEntity<?> createFeed(@ApiIgnore Authentication auth, /*@RequestBody*/ FeedPostDto feedPostDto){
        final Long userSeq = (Long)auth.getPrincipal();
        feedService.createFeed(userSeq, feedPostDto);
        return ResponseEntity.ok().body(ResponseDto.of(CREATE_FEED_SUCCESS));
    }

    @ApiOperation(value="전체 게시글 조회")
    @GetMapping("")
    public ResponseEntity<?> getALlFeeds(@ApiIgnore Authentication auth){
        final Long userSeq = (Long)auth.getPrincipal();
        final List<FeedResponseDto> feedResponseDtos = feedService.getAllFeeds(userSeq);
        return ResponseEntity.ok().body(ResponseDto.of(GET_ALL_FEED_SUCCESS, feedResponseDtos));
    }
    
    @ApiOperation(value = "내가 작성한 게시글 조회")
    @GetMapping("/writer")
    public ResponseEntity<ResponseDto> getMyFeeds(@ApiIgnore Authentication auth, @PageableDefault Pageable pageable){
        final Long userSeq = (Long)auth.getPrincipal();
        final PagingResultDto pagingResultDto = feedService.getMyFeeds(userSeq, pageable);
        return ResponseEntity.ok().body(ResponseDto.of(GET_MY_FEED_SUCCESS, pagingResultDto));
    }

    @ApiOperation(value = "내가 스크랩한 게시글 조회")
    @GetMapping("/scrap")
    public ResponseEntity<ResponseDto> getScrapFeeds(@ApiIgnore Authentication auth, @PageableDefault Pageable pageable){
        final Long userSeq = (Long)auth.getPrincipal();
        final PagingResultDto pagingResultDto = feedService.getScrapFeeds(userSeq, pageable);
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
    public ResponseEntity<?> deleteFeed(@PathVariable(value = "feed_seq") Long feedSeq){
        feedService.deleteFeed(feedSeq);
        return ResponseEntity.ok().body(ResponseDto.of(DELETE_FEED_SUCCESS));
    }

    @ApiOperation(value = "게시글 스크랩")
    @PostMapping("/scrap/{feed_seq}")
    public ResponseEntity<?> scrapFeed(@ApiIgnore Authentication auth, @PathVariable(value = "feed_seq") Long feedSeq){
        final Long userSeq = (Long)auth.getPrincipal();
        feedService.scrapFeed(userSeq, feedSeq);
        return ResponseEntity.ok().body(ResponseDto.of(UPDATE_SCRAP_FEED_SUCCESS));
    }

    @ApiOperation(value = "게시글 스크랩 취소")
    @DeleteMapping("/scrap/{feed_seq}")
    public ResponseEntity<?> unscrapFeed(@ApiIgnore Authentication auth, @PathVariable(value = "feed_seq") Long feedSeq){
        final Long userSeq = (Long)auth.getPrincipal();
        feedService.unscrapFeed(userSeq, feedSeq);
        return ResponseEntity.ok().body(ResponseDto.of(UPDATE_UNSCRAP_FEED_SUCCESS));
    }

    @ApiOperation(value = "게시글 좋아요")
    @ApiResponses({
            @ApiResponse(code = 200, message = "F006 - 게시물 좋아요에 성공하였습니다."),
            @ApiResponse(code = 401, message = "M003 - 로그인이 필요한 화면입니다."),
    })
    @PostMapping("/like")
    public ResponseEntity<ResponseDto> likeFeed(@ApiIgnore Authentication auth, @RequestParam Long feedSeq){
        final Long userSeq = (Long)auth.getPrincipal();

        final boolean success = feedService.likeFeed(userSeq, feedSeq);
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
    public ResponseEntity<ResponseDto> unlikeFeed(@ApiIgnore Authentication auth, @RequestParam Long feedSeq){
        final Long userSeq = (Long)auth.getPrincipal();

        final boolean success = feedService.unlikeFeed(userSeq, feedSeq);
        if (success) {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.UPDATE_UNGOOD_FEED_SUCCESS, success));
        } else {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.UPDATE_UNGOOD_FEED_FAIL, success));
        }
    }
}
