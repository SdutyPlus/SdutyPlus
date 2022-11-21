<div align="center">

<img src="https://user-images.githubusercontent.com/49026286/202903659-84b39720-96f9-4a7c-8ea8-80c8e299ad35.png" width="130" height="130"/>


# Sduty+ 

**공부 인증 스터디앱**

**[1. Explore Main Repository](./)**<br>
**[2. Explore Front Repository](./AOS)**

</div>

## Response 규칙

### 💥 Error Response
```
{
  "status": 400,
  "code": "U001",
  "message": "존재하지 않는 유저입니다."
}
```
### 💥 Error Code
```
public enum ErrorCode {
    // Global
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

    // 중략 ...

    ;

    private final int status;
    private final String code;
    private final String message;
}
```
<br>

### 🌐 Result Response
```
{
  "status": 200,
  "code": "W001",
  "message": "신고가 완료되었습니다.",
  "data": true
}
```

### 🌐 Result Code
```
public enum ResultCode {
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
    GET_REPORT_SUCCESS(200, "T008", "리포트 조회에 성공하였습니다."),

    // 중략

    ;

    private final int status;
    private final String code;
    private final String message;
}
```

