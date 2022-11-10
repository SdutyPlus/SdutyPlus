package com.d205.domain.usecase.report

import com.d205.domain.model.report.Task
import com.d205.domain.repository.ReportRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val reportRepository: ReportRepository
) {
    operator fun invoke(task_seq: Long, task: Task): Flow<Boolean> = flow {
        reportRepository.updateTask(task_seq, task).collect {
            if(it is ResultState.Success){
                emit(it.data)
            } else{
                emit(false)
            }
        }
    }
}