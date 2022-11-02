package com.d205.domain.repository

import com.d205.domain.model.report.Report

interface ReportRepository {
    suspend fun getReportList(date: String): Report
}