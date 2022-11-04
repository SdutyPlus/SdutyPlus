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
    //Task
    CREATE_TASK_SUCCESS(200, "T001", "테스크가 생성되었습니다."),
    UPDATE_TASK_SUCCESS(200, "T002", "테스크가 수정되었습니다.");

    private final int status;
    private final String code;
    private final String message;
}
