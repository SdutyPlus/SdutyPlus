package com.d108.sduty.model.dto

data class Follow(var followerSeq: Int, var followeeSeq: Int, var profile: Profile?) {
    // 팔로우 등록/취소용
    constructor(followerSeq: Int, followeeSeq: Int):
            this( followerSeq, followeeSeq, null)
}