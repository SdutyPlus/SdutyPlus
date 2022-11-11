package com.d205.sdutyplus.uitls

import com.d205.domain.model.common.JobHashtag

const val SERVER_URL = "https://d205.kro.kr/api/"

const val COMMON_JOIN = 0
const val KAKAO_JOIN = 1
const val NAVER_JOIN = 2

const val PROFILE = 0
const val NOT_PROFILE = 1

const val FLAG_GALLERY = 0
const val FLAG_CAMERA = 1
const val FLAG_NO_SELECT = 2

val jobs = mutableListOf(
    JobHashtag(1,"학생"),
    JobHashtag(2,"공무원"),
    JobHashtag(3,"개발자"),
    JobHashtag(4,"회사원"),
    JobHashtag(5,"연구원"),
    JobHashtag(6,"기타")
)

const val ALL_STORY = 0
const val SCRAP_STORY = 1

