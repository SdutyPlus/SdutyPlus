package com.d205.sdutyplus.view.report

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.d205.domain.model.report.Task
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentReportBinding
import com.d205.sdutyplus.view.report.dialog.TaskDialog
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ReportFragment : BaseFragment<FragmentReportBinding>(R.layout.fragment_report),
    TaskAdapterListener {
    private val reportViewModel by viewModels<ReportViewModel>()
    private val taskAdapter = TaskAdapter(this)
    private val today = LocalDate.now()


    override fun initOnViewCreated() {
        binding.apply {
            reportVM = reportViewModel
            rvReport.adapter = taskAdapter
            tvSelectedDate.text = today.toString()
        }
        lottie()
        initView()
        initViewModelCallback()
        initClickListener()

    }

    private fun initViewModelCallback() {
        reportViewModel.taskCheck.observe(viewLifecycleOwner) {
            if (it) {
                binding.apply {
                    lottie.visibility = View.GONE
                    scrollTask.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    lottie.visibility = View.VISIBLE
                    scrollTask.visibility = View.GONE
                }
            }
        }
    }

    private fun initView() {
        reportViewModel.apply {
            getReportTotalTime(binding.tvSelectedDate.text.toString())
            getTaskList(binding.tvSelectedDate.text.toString())
        }
    }


    private fun initClickListener() {
        binding.ivCalendarCall.setOnClickListener {
            val dialog = CalendarBottomSheetFragment(binding.tvSelectedDate.text.toString())
            dialog.show(parentFragmentManager, "BottomSheet")
            dialog.setOnClickListener {
                binding.tvSelectedDate.text = it
                initView()
            }
        }
    }

    private fun lottie() {
        binding.lottie.playAnimation()
    }

    override fun onTaskClicked(task: Task) {
        TaskDialog(task).apply {
            arguments = Bundle().apply {
                putString("Action", "Info")
            }
            show(this@ReportFragment.parentFragmentManager, "TaskDialog")
        }
    }

}