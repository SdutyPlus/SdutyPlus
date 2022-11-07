package com.d205.sdutyplus.view.report

import com.d205.domain.model.report.Task

interface TaskAdapterListener {
    fun onTaskClicked(task: Task)
}