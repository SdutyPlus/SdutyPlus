package com.d205.data.mapper

import com.d205.data.api.BaseResponse
import com.d205.data.model.report.ReportResponse
import com.d205.data.model.study.StudyResponse
import com.d205.domain.model.report.Report
import com.d205.domain.model.report.Task
import com.d205.domain.model.study.Study
import com.d205.domain.repository.ReportRepository
import retrofit2.Response

fun mapperToReport(reportResponse: BaseResponse<ReportResponse>): String {
    return reportResponse.data.totalTime
}

fun mapperToTask(reportResponse: BaseResponse<ReportResponse>): List<Task> {
    return reportResponse.data.taskDtos
}
