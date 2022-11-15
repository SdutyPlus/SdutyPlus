package com.d205.domain.model.feed

data class Writer(
    val userSeq: Long,
    val email: String,
    val nickname: String,
    val job: String,
    val imgUrl: String
)