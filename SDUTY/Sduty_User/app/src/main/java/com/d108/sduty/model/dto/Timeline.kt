package com.d108.sduty.model.dto

data class Timeline(var profile: Profile, var story: Story, var cntReply: Int, var replies: MutableList<Reply>, var numLikes: Int, var likes: Boolean, var scrap: Boolean, var jobHashtag: JobHashtag, var interestHashtags: List<InterestHashtag>) {
}