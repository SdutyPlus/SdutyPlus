package com.d205.data.model.report

data class TaskResponse (
    val seq: Long,
    val startTime: String,
    val endTime: String,
    val title: String,
    val contents: List<String>
)