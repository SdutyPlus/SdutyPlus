package com.d205.sdutyplus.view.report

import com.d205.domain.model.report.SubTask

interface SubTaskAdapterListener {
    fun onSubTaskClicked(subTask: SubTask)
}