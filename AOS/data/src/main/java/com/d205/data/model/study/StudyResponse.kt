package com.d205.data.model.study

import java.util.*

data class StudyResponse(
    var seq: Int,
    var masterSeq: Int,
    var alarm: Int?,
    var name: String,
    var introduce: String,
    var category: String,
    var limitNumber: Int,
    var joinNumber: Int,
    var password: String?,
    var studyRegtime: Date?,
    var notice: String,
    var roomId: String?,
    var masterNickname: String,
    var masterJob: String
)