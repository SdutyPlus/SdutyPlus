package com.d205.sdutyplus.view.report

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.report.Report
import com.d205.domain.model.report.Task
import com.d205.domain.usecase.report.GetReportUseCase
import com.d205.domain.usecase.report.GetTaskListUseCase
import com.d205.domain.utils.ResultState
import com.d205.sdutyplus.uitls.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.security.PrivateKey
import javax.inject.Inject

const val TAG = "ReportViewModel"
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getReportUseCase: GetReportUseCase,
    private val getTaskListUseCase: GetTaskListUseCase
): ViewModel() {

    private val _today = SingleLiveEvent<String>()
    val today get() = _today

    private val _totalTime = SingleLiveEvent<String?>()
    val totalTime get() = _totalTime

    private val _remoteTask: MutableStateFlow<ResultState<List<Task>>> = MutableStateFlow(ResultState.Uninitialized)
    val remoteTask get() = _remoteTask.asStateFlow()

    fun getReportTotalTime(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getReportUseCase(date).collectLatest {
                if(it is ResultState.Success){
                    _totalTime.postValue(it.data)
                }
            }
        }
    }

    fun getReportList(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getTaskListUseCase(date).collectLatest {
                if(it is ResultState.Success){
                    Log.d(TAG, "getReportList: $it")
                    _today.postValue(date)
                    _remoteTask.value = it
                }
            }
        }
    }

}