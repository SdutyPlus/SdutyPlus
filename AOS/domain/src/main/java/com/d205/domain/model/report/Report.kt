package com.d205.domain.model.report


data class Report(
    val seq: Int,
    val totalTime: String,
    var task: List<Task>
)