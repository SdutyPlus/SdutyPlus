package com.d205.domain.model.mypage

import java.util.*

data class Profile(var userSeq: Int = 2,
                   var nickname: String,
                   var shortIntroduce: String,
                   var imgUrl: String,
                   var job: Int,
                   var blockAction: Int,
                   var warning: Int,
                   var isProhibitedUser: Int,
                   var isStudying: Int,
                   var cntStory: Int

)
