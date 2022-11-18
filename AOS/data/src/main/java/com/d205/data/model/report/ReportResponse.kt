package com.d205.data.model.report

import com.d205.domain.model.report.Task

data class ReportResponse(
    val totalTime: String,
    val percentage: Int,
    var taskDtos: List<Task>
)