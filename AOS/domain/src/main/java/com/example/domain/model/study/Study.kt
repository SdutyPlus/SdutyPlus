package com.example.domain.model.study

import java.util.*

data class Study(
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
) {
    constructor(
        masterSeq: Int,name: String, introduce: String, category: String, limitNumber: Int,
        password: String?, roomId: String?):
            this(0, masterSeq, null ,name, introduce, category,
            limitNumber, 0, password, null, "공지사항", roomId, "", "")

    //constructor(): this(51,"e","e","대학생",6,"e",null)
    //constructor(): this(0,"구미 1반 8팀","안녕하세요","SSAFY",6,"권용준","") // 영상촬영용. 삭제해야됨

}