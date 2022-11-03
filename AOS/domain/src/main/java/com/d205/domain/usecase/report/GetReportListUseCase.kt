package com.d205.domain.usecase.report

import com.d205.domain.repository.ReportRepository
import javax.inject.Inject

class GetReportListUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    operator fun invoke(date: String) = reportRepository.getReportList(date)
}