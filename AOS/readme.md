<div align="center">

<img src="https://user-images.githubusercontent.com/49026286/202903659-84b39720-96f9-4a7c-8ea8-80c8e299ad35.png" width="130" height="130"/>


# Sduty+ 

**공부 인증 스터디앱**

**[1. Explore Main Repository](./)**<br>
**[2. Explore Back Repository](./Backend)**

</div>

<br>

## 클린 아키텍처

[클린 아키텍쳐]
flow
coroutine
ResultState
ViewModel

<br>

## 사용한 라이브러리
<table>
	<tr><th rowspan="1">라이브러리명</th><th rowspan="1">Version</th><th rowspan="1">Content</th></tr>
	<tr><td>Retrofit</td><td>2.9.0</td><td>Http통신을 위한 라이브러리</td></tr>
	<tr><td>Navigation</td><td>2.5.2</td><td>UI 전환을 쉽게 구현하는데 도와주는 라이브러리</td></tr> 
	<tr><td>Permission</td><td>3.3.0</td><td>권한 체크 라이브러리</td></tr>
	<tr><td>Paging3</td><td>3.1.1</td><td>다양한 데이터 소스에서 대량의 data set를 효율적으로 관리하고 로드하기위한 라이브러리</td></tr>
	<tr><td>lottie</td><td>3.0.7</td><td>고품질 애니메이션 라이브러리입니다</td></tr>
	<tr><td>Glide</td><td>4.13.0</td><td>이미지 로딩 라이브러리</td></tr>
	<tr><td>CircleImageView</td><td>3.1.0</td><td>ImageView를 확장한 라이브러리</td></tr>
	<tr><td>ImagePicker</td><td>2.1</td><td>갤러리 or 카메라를 통해 이미지를 가져오고 화면에 표시하기 위해 사용하는 라이브러리</td></tr>
	<tr><td>ImageCrop</td><td>4.3.1</td><td>이미지 Crop 라이브러리</td></tr>
	<tr><td>SwipeRefreshLayout</td><td>1.1.0</td><td>화면의 컨텐츠를 새로고침할 때 사용되는 라이브러리</td></tr>
	<tr><td>kizitonwose</td><td>2.0.1</td><td>커스텀 캘린더 라이브러리</td></tr>
	<tr><td>MPAndroidChart</td><td>3.1.0</td><td>앱을 위한 차트 라이브러리</td></tr>
	<tr><td>material</td><td>1.8.0 - alpha01</td><td>Google 에서 공식적으로 제공하는 디자인 라이브러리</td></tr>
	<tr><td>Kakao</td><td>2.11.0</td><td>Kakao 로그인 라이브러리</td></tr>
	<tr><td>Naver</td><td>5.1.1</td><td>Naver 로그인 라이브러리</td></tr>
	 

</table>



<br>

## 디렉토리 구조
```markdown
📁Backend/sdutyplus
├──📁src/main
├──📁java/com/d205/sdutyplus
│ ├──📃SdutyplusApplication.java
│ ├──📁domain
│ │ ├──📁admin
│ │ ├──📁feed
│ │ ├──📁jwt
│ │ ├──📁off
│ │ ├──📁statistics
│ │ ├──📁task
│ │ ├──📁timer
│ │ ├──📁user
│ │ └──📁warn
│ ├──📁global
│ │ ├──📁config
│ │ ├──📁dto
│ │ ├──📁entity
│ │ ├──📁enums
│ │ ├──📁error
│ │ ├──📁policy
│ │ └──📁response
│ └──📁util
│   ├──📁AuthUtils
│   ├──📁FirebaseInitializer
│   ├──📁MD5Generator
│   ├──📁TimeFormatter
│   └──📁Workaround
├──📃Dockerfile
├──📃Jenkinsfile
├──📃.gitignore
└──📃build.gradle
```
