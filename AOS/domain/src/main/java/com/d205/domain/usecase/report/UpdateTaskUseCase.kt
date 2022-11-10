package com.d205.domain.usecase.report

import com.d205.domain.model.report.Task
import com.d205.domain.repository.ReportRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    operator fun invoke(task_seq: Long, task: Task) = reportRepository.updateTask(task_seq, task)
}