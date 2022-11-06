package com.d205.domain.model.report


data class Report(
    val totalTime: String,
    var taskDtos: List<Task>
)