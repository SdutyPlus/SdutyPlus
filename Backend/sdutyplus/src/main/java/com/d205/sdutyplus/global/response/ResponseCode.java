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
    LOGIN_SUCCESS(200, "U001", "로그인에 성공하였습니다."),
    GET_USERPROFILE_SUCCESS(200, "U002", "회원 프로필을 조회하였습니다."),
    UPLOAD_USER_IMAGE_SUCCESS(200, "U003", "회원 이미지를 등록하였습니다."),
    EDIT_PROFILE_SUCCESS(200, "U004", "회원 프로필을 수정하였습니다."),
    CHECK_NICKNAME_GOOD(200, "U005", "사용가능한 nickname 입니다."),
    CHECK_NICKNAME_BAD(200, "U006", "사용불가능한 nickname 입니다."),
    LOGIN_FAIL(200, "U007", "로그인에 실패하였습니다."),
    SAVE_PROFILE_SUCCESS(200, "U008", "회원 프로필을 저장하였습니다."),
    DELETE_SUCCESS(200, "U009", "회원 탈퇴에 성공하였습니다."),
    DELETE_FAIL(200, "U010", "회원 탈퇴에 실패하였습니다."),

    // Task
    CREATE_TASK_SUCCESS(200, "T001", "테스크가 생성되었습니다."),
    UPDATE_TASK_SUCCESS(200, "T002", "테스크가 수정되었습니다."),
    DELETE_TASK_SUCCESS(200, "T003", "테스크가 삭제되었습니다."),
    CREATE_SUBTASK_SUCCESS(200, "T004", "서브테스크가 생성되었습니다."),
    UPDATE_SUBTASK_SUCCESS(200, "T005", "서브테스크가 수정되었습니다."),
    DELETE_SUBTASK_SUCCESS(200, "T006", "서브테스크가 삭제되었습니다."),
    GET_TASK_DETAIL_SUCCESS(200, "T007", "테스크 상세 조회에 성공하였습니다."),
    GET_REPORT_SUCCESS(200, "T008", "리포트 조회에 성공하였습니다."),
    GET_REPORT_TOTALTIME_SUCCESS(200, "T009", "리포트 총 시간 조회에 성공하였습니다."),

    //Timer
    GET_LOCALDATETIME_SUCCESS(200, "TI001", "서버 날짜, 시간 조회가 성공하였습니다."),

    //Feed
    CREATE_FEED_SUCCESS(200, "F001", "게시글이 등록되었습니다."),
    GET_ALL_FEED_SUCCESS(200, "F002", "전체 게시글 조회가 성공하였습니다."),
    GET_MY_FEED_SUCCESS(200, "F003", "자신이 작성한 게시글 조회가 성공하였습니다."),
    GET_SCRAP_FEED_SUCCESS(200, "F004", "자신이 스크랩한 게시글 조회가 성공하였습니다."),
    GET_JOB_FILTER_FEED_SUCCESS(200, "F005", "직업으로 필터링한 게시글 조회가 성공하였습니다."),
    UPDATE_FEED_SUCCESS(200, "F006", "게시글이 수정되었습니다."),
    DELETE_FEED_SUCCESS(200, "F007", "게시글이 삭제되었습니다."),
    UPDATE_GOOD_FEED_SUCCESS(200, "F008", "해당 게시글에 좋아요가 등록되었습니다."),
    UPDATE_UNGOOD_FEED_SUCCESS(200, "F009", "해당 게시글에 좋아요 취소하였습니다."),
    UPDATE_SCRAP_FEED_SUCCESS(200, "F010", "해당 게시글을 스크랩하였습니다."),
    UPDATE_UNSCRAP_FEED_SUCCESS(200, "F011", "해당 게시글을 스크랩 취소하였습니다."),
    UPDATE_GOOD_FEED_FAIL(200, "F012", "좋아요 실패."),
    UPDATE_UNGOOD_FEED_FAIL(200, "F013", "좋아요 취소 실패."),

    // Warn
    WARN_SUCCESS(200, "W001", "신고가 완료되었습니다."),
    WARN_FAIL(200, "W002", "신고가 실패하였습니다."),

    // Off
    OFF_SUCCESS(200, "O001", "차단이 완료되었습니다"),
    OFF_FAIL(200, "O002", "차단이 실패하였습니다."),

    // Admin

    GET_WARN_FEED_SUCCESS(200, "A001", "신고 게시글 조회가 성공하였습니다."),
    GET_WARN_FEED_FAIL(200, "A002", "신고 게시글 조회가 실패하였습니다."),
    GET_WARN_USER_SUCCESS(200, "A003", "신고 유저 조회가 성공하였습니다."),
    BAN_WARN_USER_SUCCESS(200, "A004", "신고 유저 제재가 성공하였습니다."),
    BAN_WARN_USER_FAIL(200, "A005", "신고 유저 제재가 실패하였습니다."),


    ;

    private final int status;
    private final String code;
    private final String message;
}
