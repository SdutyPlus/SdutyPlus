package com.d205.sdutyplus.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ResponseCode Convention
 * - 도메인 별로 나누어 관리
 * - [동사_목적어_SUCCESS] 형태로 생성
 * - 코드는 도메인명 앞에서부터 1~2글자로 사용
 * - 메시지는 "~~다."로 마무리
 */

@Getter
@AllArgsConstructor
public enum ResponseCode {
    // User

    // Task
    CREATE_TASK_SUCCESS(200, "T001", "테스크가 생성되었습니다."),
    UPDATE_TASK_SUCCESS(200, "T002", "테스크가 수정되었습니다."),
    DELETE_TASK_SUCCESS(200, "T003", "테스크가 삭제되었습니다."),
    CREATE_SUBTASK_SUCCESS(200, "T004", "서브테스크가 생성되었습니다."),
    UPDATE_SUBTASK_SUCCESS(200, "T005", "서브테스크가 수정되었습니다."),
    DELETE_SUBTASK_SUCCESS(200, "T006", "서브테스크가 삭제되었습니다."),
    GET_DAILYTASK_SUCCESS(200, "T007", "데일리 테스크 조회에 성공하였습니다."),

    //Timer
    GET_LOCALDATETIME_SUCCESS(200, "TI001", "서버 날짜, 시간 조회가 성공하였습니다."),

    //Feed
    CREATE_FEED_SUCCESS(200, "F001", "게시글이 등록되었습니다."),
    GET_ALL_FEED_SUCCESS(200, "F002", "전체 게시글 조회가 성공하였습니다."),
    UPDATE_FEED_SUCCESS(200, "F003", "게시글이 수정되었습니다."),
    GET_MY_FEED_SUCCESS(200, "F004", "자신이 작성한 게시글 조회가 성공하였습니다."),
    GET_SCRAP_FEED_SUCCESS(200, "F005", "자신이 스크랩한 게시글 조회가 성공하였습니다."),
    GET_JOB_FILTER_FEED_SUCCESS(200, "F006", "직업으로 필터링한 게시글 조회가 성공하였습니다."),
    DELETE_FEED_SUCCESS(200, "F007", "게시글이 삭제되었습니다."),
    UPDATE_GOOD_FEED_SUCCESS(200, "F008", "해당 게시글에 좋아요가 등록되었습니다."),
    UPDATE_UNGOOD_FEED_SUCCESS(200, "F009", "해당 게시글에 좋아요 취소하였습니다."),
    UPDATE_SCRAP_FEED_SUCCESS(200, "F010", "해당 게시글을 스크랩하였습니다."),
    UPDATE_UNSCRAP_FEED_SUCCESS(200, "F011", "해당 게시글을 스크랩 취소하였습니다."),

    // Warn
    WARN_SUCCESS(200, "W001", "신고 완료."),
    WARN_FAIL(200, "W002", "신고 실패"),

    ;

    private final int status;
    private final String code;
    private final String message;
}
