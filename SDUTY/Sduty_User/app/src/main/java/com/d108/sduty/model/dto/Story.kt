package com.d108.sduty.model.dto

import com.fasterxml.jackson.annotation.JsonFormat

data class Story(
    val seq: Int,
    var writerSeq: Int,
    var imageSource: String,
    var thumbnail: String,
    var jobHashtag: Int?,
    var contents: String,
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ")
    var regtime: String?,
    var storyPublic: Int,
    var storyWarning: Int,
    var interestHashtag: MutableList<Int>?
) {
    constructor():
            this(0,0,",",",",0,"",null,0,0, null)
    constructor(seq: Int):
            this(seq,0,"","",0,"",null, 0, 0, null)
    constructor(writerSeq: Int, imageSource: String, jobHashtag: Int?, contents: String, storyPublic: Int, interestHashtag: MutableList<Int>?): this(
        0, writerSeq, imageSource, "", jobHashtag, contents, null, storyPublic, 0, interestHashtag)

}