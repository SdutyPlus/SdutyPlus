package com.d205.sdutyplus.view.report

import com.d205.domain.model.report.Task

interface ReportAdapterListener {
    fun onItemClicked(task: Task)
}