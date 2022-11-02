package com.d205.sdutyplus.view.report

import androidx.lifecycle.ViewModel
import com.d205.domain.usecase.report.GetReportListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val getReportListUseCase: GetReportListUseCase
): ViewModel() {
    suspend fun get(date: String) {
        getReportListUseCase(date)
    }
}