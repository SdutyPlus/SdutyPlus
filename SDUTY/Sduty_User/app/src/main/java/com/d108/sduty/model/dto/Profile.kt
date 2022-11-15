package com.d108.sduty.model.dto

import java.util.*

data class Profile(var userSeq: Int = 2,
                   var nickname: String,
                   var birthday: Date,
                   var publicBirthday: Int = 2,
                   var shortIntroduce: String,
                   var image: String,
                   var job: String,
                   var publicJob: Int = 2,
                   var interest: Int,
                   var publicInterest: Int = 2,
                   var followers: Int,
                   var followees: Int,
                   var mainAchievmentSeq: Int?,
                   var blockAction: Int,
                   var warning: Int,
                   var isProhibitedUser: Int,
                   var isStudying: Int,
                   var interestHashtagSeqs: MutableList<Int>?,
                   var interestHashtags: List<InterestHashtag>?,
                   var follows: Map<String, Follow>?,
                   var cntStory: Int

) {
    // 프로필 등록용
    constructor(userSeq: Int, nickname: String, birthday: Date, public_birth: Int, short_introduce: String, image: String, job: String, public_job: Int, public_interest: Int, main_achievement_seq: Int?, interestHashtagSeqs: MutableList<Int>?)
            :this(userSeq, nickname, birthday, public_birth, short_introduce, image, job, public_job, 0, public_interest, 0, 0, main_achievement_seq, 0, 0, 0,0, interestHashtagSeqs, null, null,0)
}
