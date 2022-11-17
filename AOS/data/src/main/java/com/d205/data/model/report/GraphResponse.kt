package com.d205.data.model.report

data class GraphResponse(
    val continuous : Int,
    val studyTime: Int,
    val dailyTimeGraphs: List<Int>
)