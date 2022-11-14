package com.d205.domain.usecase.report

import com.d205.domain.model.report.Task
import com.d205.domain.repository.ReportRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    operator fun invoke(task_seq: Long) = reportRepository.deleteTask(task_seq)
}