package com.d205.data.model.timer

data class RealTimeResponse(
    val status: Int,
    val code: String,
    val message: String,
    val data: String
)