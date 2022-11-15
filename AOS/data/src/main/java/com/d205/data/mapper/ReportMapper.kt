package com.d205.data.mapper

import com.d205.data.model.BaseResponse
import com.d205.data.model.report.ReportResponse
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
