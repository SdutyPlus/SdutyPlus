package com.d205.domain.model.timer

import com.d205.domain.model.report.SubTask

data class CurrentTaskDto(
    val seq: Int,
    val startTime: String,
    val endTime: String,
    var title: String = "",
    var contents: List<SubTask> = listOf()
)

data class CurrentTaskDto2(
    val seq: Int,
    val startTime: String,
    val endTime: String,
    var title: String = "",
    var contents: List<String> = listOf()
)