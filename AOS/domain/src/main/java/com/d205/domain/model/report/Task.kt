package com.d205.domain.model.report

data class Task (
    val seq: Int,
    val startTime: String,
    val endTime: String,
    val content: String,
    var subTask: List<SubTask>
)