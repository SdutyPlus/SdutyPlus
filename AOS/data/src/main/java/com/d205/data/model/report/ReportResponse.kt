package com.d205.data.model.report

import com.d205.domain.model.report.Task

data class ReportResponse(
    var seq: Int,
    val totalTime: String,
    var task: List<Task>
)