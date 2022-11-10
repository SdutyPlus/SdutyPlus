package com.d205.sdutyplus.view.report

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.report.Task
import com.d205.domain.usecase.report.GetReportUseCase
import com.d205.domain.usecase.report.GetTaskListUseCase
import com.d205.domain.usecase.report.UpdateTaskUseCase
import com.d205.domain.utils.ResultState
import com.d205.sdutyplus.uitls.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "ReportViewModel"

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getReportUseCase: GetReportUseCase,
    private val getTaskListUseCase: GetTaskListUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase
) : ViewModel() {

    private val _totalTime = SingleLiveEvent<String?>()
    val totalTime get() = _totalTime

    private val _remoteTask: MutableStateFlow<ResultState<List<Task>>> =
        MutableStateFlow(ResultState.Uninitialized)
    val remoteTask get() = _remoteTask.asStateFlow()

    private var _taskCheck = SingleLiveEvent<Boolean>()
    val taskCheck get() = _taskCheck

    private var _updateTaskSuccess = SingleLiveEvent<Boolean>()
    val updateTaskSuccess get() = _updateTaskSuccess

    fun getReportTotalTime(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getReportUseCase(date).collectLatest {
                if (it is ResultState.Success) {
                    _totalTime.postValue(it.data)
                }
            }
        }
    }

    fun getTaskList(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getTaskListUseCase(date).collectLatest {
                if (it is ResultState.Success) {
                    Log.d(TAG, "getTaskList: $it")
                    _remoteTask.value = it
                    if (it.data.isEmpty()) {
                        _taskCheck.postValue(false)
                    } else {
                        _taskCheck.postValue(true)
                    }
                }
            }
        }
    }

    fun updateTask(task_seq: Long, task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTaskUseCase(task_seq, task).collectLatest {
                Log.d(TAG, "updateTask: ${it}")
                if(it is ResultState.Success){
                    Log.d(TAG, "updateTask22: ${it.data}")
                    _updateTaskSuccess.postValue(true)
                }
            }
        }
    }


}