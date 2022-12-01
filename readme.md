<div align="center">

<br>
<img src="https://user-images.githubusercontent.com/49026286/202903659-84b39720-96f9-4a7c-8ea8-80c8e299ad35.png" width="200" height="200"/>
<br>

# Sduty+ 

**공부 인증 스터디앱**

**[1. Explore Front Repository](./AOS)**<br>
**[2. Explore Back Repository](./Backend)**
tes
</div>

<br>

## 기획 배경
- 높은 스터디 앱의 사용률
- 공부에 대한 정보 및 자극 요소의 제공

<br>

## 프로젝트 소개

### ⏱ 타이머
<img src="https://user-images.githubusercontent.com/49026286/202905679-e269b04f-0b62-4d92-94bb-3be739a6eaa1.gif" width="300" height="610"/>
<img src="https://user-images.githubusercontent.com/49026286/202933761-d9f2ba67-d668-4be4-b5a7-0a1ef1fa6feb.gif" width="300" height="610"/>


실제 공부하는 시간을 측정하는 타이머를 통해 공부 시간을 측정하고 기록을 저장할 수 있어요.

### 📙 리포트
<img src="https://user-images.githubusercontent.com/49026286/202905787-523feaa6-f498-4983-911c-bfe87f69b0d6.gif" width="300" height="610"/>
<img src="https://user-images.githubusercontent.com/49026286/202906067-c4f8a5e4-a72e-44f8-8069-44b1a4d7c972.png" width="300" height="610"/>

타이머로 기록된 특정 일자의 공부 시간과 내용을 리포트를 통해 확인할 수 있어요.


### ⌛ 타임라인
<img src="https://user-images.githubusercontent.com/49026286/202906189-e0fcc9c0-6187-4199-bbe7-ab8a1c0e4872.png" width="300" height="610"/>
<img src="https://user-images.githubusercontent.com/49026286/202933701-c05e504a-9d1d-4491-95cc-faefd2954f0c.jpg" width="300" height="610"/>

다른 사람들과 공부한 내용을 공유할 수 있어요.

### 📊 통계
<img src="https://user-images.githubusercontent.com/49026286/202933789-ed946a7a-5a5d-4f03-a24b-29f4d204e8cb.gif" width="300" height="610"/>

오늘 하루 공부한 결과를 통계 자료를 통해 시각적으로 볼 수 있어요.

<br>


## 시스템 구조도
<div align="center">

<img src="https://user-images.githubusercontent.com/49026286/202906377-f1317e67-ebb5-4aef-a444-806d1eee1bab.png" width="900" height="600"/>

</div>
<br>

## 규칙

🌐 Git Flow
```
master : 제품으로 출시될 수 있는 브랜치
release : 이번 출시 버전을 준비하는 브랜치
develop : 다음 출시 버전을 개발하는 브랜치
feature : 기능을 개발하는 브랜치
hotfix : 출시 버전에서 발생한 버그를 수정 하는 브랜치
```


🌐 Git branch 생성 규칙

master ← develop ←  (release) ← be/fe ← feat  **순서대로 머지** 한다.


개발 시 **feat-기능 이름** 으로 브랜치를 만들어 상위에 머지한다.

- BE/**feat/naver-login**
- FE/**feat/naver-login**

UI만 작업을 할때는 가장 뒤에 UI를 작성한다.

- FE/**feat/naver-login-UI**


🌐 Git 커밋 컨벤션 생성 규칙
```
feat : 기능 추가
fix : 버그 수정
docs : 문서 수정
style : 단순 수정 (세미콜론, 코드 이동, 띄어 쓰기, 이름 변경)
rename: 추가된 기능, 별 내용 없이 폴더 및 파일만 추가, 폴더 및 파일 이름 수정, 옮기기
refactor : 코드 리팩토링
test : 테스트 추가
study : 학습 내용
```
**ver01(간략) : commit type: Epic/대분류 | 작업 단위 [Jira 이슈 번호]**


feat: 회원관리 | 네이버 로그인 기능 추가  [Jira 이슈번호]

**ver02(설명) : ver01양식(Jira 번호 빼고) - 본문 - [Jira 이슈 번호]**

feat: 회원관리 | 네이버 로그인 기능 추가

본문은 위, 아래 한 줄 띄우고 원하는 대로 작성한다.

 => 이런 식으로 특수기호를 쓰거나 이모티콘을 쓰는 것도 가능하다. 😄

단, 한 줄에 너무 길지 않도록 작성하자.

[Jira 이슈 번호]

<br/>

