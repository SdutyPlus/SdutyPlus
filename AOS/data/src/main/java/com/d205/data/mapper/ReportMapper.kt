package com.d205.data.mapper

import com.d205.data.model.report.ReportResponse
import com.d205.data.model.study.StudyResponse
import com.d205.domain.model.report.Report
import com.d205.domain.model.study.Study
import com.d205.domain.repository.ReportRepository

fun mapperToReport(reportResponse: ReportResponse): Report {
    return Report(
        seq = reportResponse.seq
    )
}