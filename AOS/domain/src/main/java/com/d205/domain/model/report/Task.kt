package com.d205.domain.model.report

data class Task (
    val seq: Long,
    var startTime: String,
    var endTime: String,
    var title: String,
    var contents: List<String> = listOf()
)