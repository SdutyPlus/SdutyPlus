package com.d205.sdutyplus.view.report

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d205.domain.usecase.report.GetReportListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

const val TAG = "ReportViewModel"
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getReportListUseCase: GetReportListUseCase
): ViewModel() {

    fun get(date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getReportListUseCase(date)
            Log.d(TAG, "get: $date")
        }

    }
}