package com.d205.domain.model.report

data class Graph (
    val continuous : Int,
    val studyTime: Int,
    val dailyTimeGraphs: List<Int>
)