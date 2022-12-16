package com.d205.sdutyplus.utills

import com.d205.domain.model.common.JobHashtag



const val COMMON_JOIN = 0
const val KAKAO_JOIN = 1
const val NAVER_JOIN = 2

const val PROFILE = 0
const val NOT_PROFILE = 1

const val FLAG_GALLERY = 0
const val FLAG_CAMERA = 1
const val FLAG_NO_SELECT = 2

val jobs = mutableListOf(
    JobHashtag(1,"학생", false),
    JobHashtag(2,"공무원", false),
    JobHashtag(3,"개발자", false),
    JobHashtag(4,"회사원", false),
    JobHashtag(5,"연구원", false),
    JobHashtag(6,"기타", false)
)

const val ALL_STORY = 0
const val SCRAP_STORY = 1
const val HOME_ALL_STORY = 2

