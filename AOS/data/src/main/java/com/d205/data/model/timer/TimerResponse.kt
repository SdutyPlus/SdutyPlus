package com.d205.data.model.timer

data class TimerResponseDto(
    val status: Int,
    val code: String,
    val message: String,
    val data: String
)