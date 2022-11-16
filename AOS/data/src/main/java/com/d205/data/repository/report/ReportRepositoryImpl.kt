package com.d205.data.repository.report

import android.util.Log
import com.d205.data.mapper.mapperToGraph
import com.d205.data.mapper.mapperToReport
import com.d205.data.mapper.mapperToTask
import com.d205.data.repository.report.remote.ReportRemoteDataSource
import com.d205.domain.model.report.Graph
import com.d205.domain.model.report.Report
import com.d205.domain.model.report.SubTask
import com.d205.domain.model.report.Task
import com.d205.domain.repository.ReportRepository
import com.d205.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

const val TAG = "ReportRepositoryImpl"

class ReportRepositoryImpl @Inject constructor(
    private val reportRemoteDataSource: ReportRemoteDataSource
): ReportRepository {
    override fun getReport(date: String): Flow<ResultState<Report>> = flow {
        emit(ResultState.Loading)
        reportRemoteDataSource.getReport(date).collect {
            emit(ResultState.Success(mapperToReport(it)))
        }
    }.catch { e ->
        emit(ResultState.Error(e))
    }

    override fun getTaskList(date: String): Flow<ResultState<List<Task>>> = flow {
        emit(ResultState.Loading)
        reportRemoteDataSource.getReport(date).collect {
            emit(ResultState.Success(mapperToTask(it)))
        }
    }.catch { e ->
        emit(ResultState.Error(e))
    }

    override fun updateTask(task_seq: Long, task: Task): Flow<ResultState<Boolean>> = flow {
        emit(ResultState.Loading)
        reportRemoteDataSource.updateTask(task_seq, task).collect {
            emit(ResultState.Success(it))
        }
    }.catch { e ->
        emit(ResultState.Error(e))
    }

    override fun deleteTask(task_seq: Long): Flow<ResultState<Boolean>> = flow {
        emit(ResultState.Loading)
        reportRemoteDataSource.deleteTask(task_seq).collect {
            emit(ResultState.Success(it))
        }
    }.catch { e ->
        emit(ResultState.Error(e))
    }

    override fun getGraph(): Flow<ResultState<Graph>> = flow {
        emit(ResultState.Loading)
        reportRemoteDataSource.getGraph().collect {
            emit(ResultState.Success(mapperToGraph(it)))
        }
    }.catch { e ->
        emit(ResultState.Error(e))
    }
}