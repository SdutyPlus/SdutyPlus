package com.d205.sdutyplus.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * ErrorCode Convention
 * - 도메인 별로 나누어 관리
 * - [주체_이유] 형태로 생성
 * - 코드는 도메인명 앞에서부터 1~2글자로 사용
 * - 메시지는 "~~다."로 마무리
 */

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //Global
    INPUT_VALUE_INVALID(400, "G001", "유효하지 않은 입력입니다."),
    INPUT_TYPE_INVALID(400, "G002", "입력 타입이 유효하지 않습니다."),
    TIME_FORMAT_INVALID(400, "G003", "날짜, 시간 타입 형식이 유효하지 않습니다."),
    JOB_NOT_FOUND(400,"G004", "존재하지 않는 직업입니다."),

    // User
    USER_NOT_FOUND(400, "U001", "존재하지 않는 유저입니다."),
    USERNAME_ALREADY_EXIST(400, "U002", "이미 존재하는 사용자 이름입니다."),
    AUTHENTICATION_FAIL(401, "U003", "로그인이 필요한 화면입니다."),
    AUTHORITY_INVALID(403, "U004", "권한이 없습니다."),
    ACCOUNT_MISMATCH(401, "U005", "계정 정보가 일치하지 않습니다."),

    // Task
    TASK_NOT_FOUND(400, "T001", "존재하지 않는 테스크입니다."),
    SUBTASK_NOT_FOUND(400, "T002", "존재하지 않는 서브 테스크입니다."),
    TASK_TIME_DUPLICATE(400, "T003", "해당 시간에 테스크가 이미 존재합니다."),

    // Feed
    IMAGE_TYPE_NOT_SUPPORT(400, "F001", "지원되지 않는 파일 형식입니다."),
    FEED_NOT_FOUND(400, "F002", "존재하지 않는 피드입니다."),
    FEED_SCRAP_NOT_FOUND(400, "F003", "스크랩하지 않은 피드입니다."),
    FEED_LIKE_ALREADY_EXIST(400, "F004", "해당 게시물에 이미 좋아요를 누른 회원입니다."),
    FEED_LIKE_NOT_FOUND(400, "F005", "좋아요 하지 않은 게시물 입니다."),

    // Warn
    WARN_ALREADY_EXIST(400, "W001", "이미 신고한 유저입니다."),
    WARN_MYSELF_FAIL(400,"W002","자기 자신은 신고 할 수 없습니다."),

    // Off
    OFF_ALREADY_EXIST(400, "O001", "이미 차단한 유저입니다."),
    OFF_MYSELF_FAIL(400, "O002", "자기 자신은 차단 할 수 없습니다."),

    // DailyStatistics
    STATISTICS_NOT_FOUND(400, "DS001", "기록이 존재하지 않습니다."),

    ;

    private final int status;
    private final String code;
    private final String message;
}
