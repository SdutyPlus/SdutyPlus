package com.d205.domain.usecase.report

import com.d205.domain.repository.ReportRepository
import javax.inject.Inject

class GetDateUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    operator fun invoke() = reportRepository.getDate()
}