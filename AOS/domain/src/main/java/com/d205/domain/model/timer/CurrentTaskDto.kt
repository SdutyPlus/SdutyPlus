package com.d205.domain.model.timer

import com.d205.domain.model.report.SubTask

data class CurrentTaskDto(
    val seq: Int,
    val startTime: String,
    val endTime: String,
    val elapsedTime: Int,
    val title: String,
    val contents: List<SubTask> = listOf()
)