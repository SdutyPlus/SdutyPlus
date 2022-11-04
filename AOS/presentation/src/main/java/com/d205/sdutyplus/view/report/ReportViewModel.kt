package com.d205.sdutyplus.view.report

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.model.report.Report
import com.d205.domain.model.report.Task
import com.d205.domain.usecase.report.GetReportListUseCase
import com.d205.domain.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "ReportViewModel"
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getReportListUseCase: GetReportListUseCase
): ViewModel() {
    private val _remoteReport: MutableStateFlow<ResultState<Report>> = MutableStateFlow(ResultState.Uninitialized)
    val remoteReport get() =_remoteReport.asStateFlow()

    fun getReportList(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getReportListUseCase(date).collectLatest {
                _remoteReport.value = it

            }
        }
    }

    fun get(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getReportListUseCase(date)
            Log.d(TAG, "get: $date")
        }

    }
}