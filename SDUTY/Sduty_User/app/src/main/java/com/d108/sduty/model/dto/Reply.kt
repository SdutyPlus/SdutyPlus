package com.d108.sduty.model.dto

import java.util.*

data class Reply(
    var seq: Int,
    var storySeq: Int,
    var userSeq: Int,
    var mentionedSeq: Int,
    var content: String,
    var regTime: Date?,
    var profile: Profile?

) {
    constructor(storySeq: Int, userSeq: Int, content: String) :
            this(0, storySeq, userSeq, 0, content, null, null)

}