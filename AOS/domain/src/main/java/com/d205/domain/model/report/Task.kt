package com.d205.domain.model.report

data class Task (
    val seq: Int,
    var startTime: String,
    var endTime: String,
    val content: String,
    var subTaskDtos: List<SubTask>
)