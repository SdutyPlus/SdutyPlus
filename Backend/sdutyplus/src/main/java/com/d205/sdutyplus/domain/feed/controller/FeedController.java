package com.d205.sdutyplus.domain.feed.controller;

import com.d205.sdutyplus.domain.feed.dto.FeedPostDto;
import com.d205.sdutyplus.domain.feed.dto.FeedResponseDto;
import com.d205.sdutyplus.domain.feed.service.FeedService;
import com.d205.sdutyplus.global.response.ResponseCode;
import com.d205.sdutyplus.global.response.ResponseDto;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
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

    @ApiOperation(value = "게시글 좋아요")
    @ApiResponses({
            @ApiResponse(code = 200, message = "F006 - 게시물 좋아요에 성공하였습니다."),
            @ApiResponse(code = 401, message = "M003 - 로그인이 필요한 화면입니다."),
    })
    @ApiImplicitParam(name = "feedId", value = "게시물 PK", example = "1", required = true)
    @PostMapping("/like")
    public ResponseEntity<ResponseDto> likeFeed(@ApiIgnore Authentication auth, @RequestParam Long feedSeq){
        long userSeq = (int)auth.getPrincipal();

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
    @ApiImplicitParam(name = "feedId", value = "게시물 PK", example = "1", required = true)
    @DeleteMapping("/like")
    public ResponseEntity<ResponseDto> unlikeFeed(@ApiIgnore Authentication auth, @RequestParam Long feedSeq){
        long userSeq = (int)auth.getPrincipal();

        final boolean success = feedService.unlikeFeed(userSeq, feedSeq);
        if (success) {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.UPDATE_UNGOOD_FEED_SUCCESS, success));
        } else {
            return ResponseEntity.ok(ResponseDto.of(ResponseCode.UPDATE_UNGOOD_FEED_FAIL, success));
        }
    }
}
