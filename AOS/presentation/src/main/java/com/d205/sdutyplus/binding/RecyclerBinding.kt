package com.d205.sdutyplus.binding

import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.d205.domain.model.report.Task
import com.d205.domain.utils.ResultState
import com.d205.sdutyplus.view.report.ReportAdapter

object RecyclerBinding {

    @BindingAdapter("submitList")
    @JvmStatic
    fun bindSubmitList(view: RecyclerView, resultState: ResultState<*>?) {

        if (resultState is ResultState.Success) {
            when (view.adapter) {
                is ReportAdapter -> {
                    (view.adapter as ListAdapter<Any, *>).submitList(resultState.data as List<Task>)
                }
            }
        } else if (resultState is ResultState.Empty) {
            (view.adapter as ListAdapter<Any, *>).submitList(emptyList())
        }
    }

}