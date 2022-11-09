package com.d205.sdutyplus.binding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d205.domain.model.report.SubTask
import com.d205.domain.model.report.Task
import com.d205.domain.utils.ResultState
import com.d205.sdutyplus.view.report.SubTaskAdapter
import com.d205.sdutyplus.view.report.TaskAdapter

object RecyclerBinding {

    @BindingAdapter("submitList")
    @JvmStatic
    fun bindSubmitList(view: RecyclerView, resultState: ResultState<*>?) {

        if (resultState is ResultState.Success) {
            when (view.adapter) {
                is TaskAdapter -> {
                    (view.adapter as ListAdapter<Any, *>).submitList(resultState.data as List<Task>)
                }
                is SubTaskAdapter -> {
                    (view.adapter as ListAdapter<Any, *>).submitList(resultState.data as List<SubTask>)
                }
            }
        } else if (resultState is ResultState.Empty) {
            (view.adapter as ListAdapter<Any, *>).submitList(emptyList())
        }
    }

}