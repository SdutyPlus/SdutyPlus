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
    //Task
    TASK_NOT_FOUND(400, "T001", "존재하지 않는 테스크입니다."),

    //Feed
    IMAGE_TYPE_NOT_SUPPORT(400, "F001", "지원되지 않는 파일 형식입니다."),
    FEED_NOT_FOUND(400, "F002", "존재하지 않는 피드입니다.");

    private final int status;
    private final String code;
    private final String message;
}
