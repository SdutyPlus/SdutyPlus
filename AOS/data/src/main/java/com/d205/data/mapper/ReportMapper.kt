package com.d205.data.mapper

import com.d205.data.model.BaseResponse
import com.d205.data.model.report.DateResponse
import com.d205.data.model.report.GraphResponse
import com.d205.data.model.report.ReportResponse
import com.d205.domain.model.report.Date
import com.d205.domain.model.report.Graph
import com.d205.domain.model.report.Report
import com.d205.domain.model.report.Task

fun mapperToReport(reportResponse: BaseResponse<ReportResponse>): Report {
    return Report(
        reportResponse.data!!.totalTime,
        reportResponse.data.percentage
    )
}

fun mapperToTask(reportResponse: BaseResponse<ReportResponse>): List<Task> {
    return reportResponse.data!!.taskDtos
}

fun mapperToDate(dateResponse: BaseResponse<DateResponse>): Date{
    return Date(
        dateResponse.data!!.date
    )
}

fun mapperToGraph(graphResponse: BaseResponse<GraphResponse>): Graph{
    return Graph(
        graphResponse.data!!.continuous,
        graphResponse.data.studyTime,
        graphResponse.data.dailyTimeGraphs
    )
}


