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
    JobHashtag(1,"신한은행"),
    JobHashtag(2,"카카오"),
    JobHashtag(3,"라인"),
    JobHashtag(4,"네이버"),
    JobHashtag(5,"싸피코치"),
    JobHashtag(6,"포켓몬트레이너")
)

const val ALL_STORY = 0
const val SCRAP_STORY = 1

