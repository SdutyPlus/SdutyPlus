package com.d205.data.repository.report

import com.d205.data.mapper.mapperToReport
import com.d205.data.repository.report.remote.ReportRemoteDataSource
import com.d205.domain.model.report.Report
import com.d205.domain.repository.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportRemoteDataSource: ReportRemoteDataSource
): ReportRepository {

    override suspend fun getReportList(date: String): Report {
        return mapperToReport(reportRemoteDataSource.getReportList(date).body()!!)
    }

}