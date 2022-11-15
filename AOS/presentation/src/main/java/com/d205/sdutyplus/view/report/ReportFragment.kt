package com.d205.sdutyplus.view.report

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.d205.domain.model.report.Task
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentReportBinding
import com.d205.sdutyplus.view.MainViewModel
import com.d205.sdutyplus.view.report.dialog.TaskDialog
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ReportFragment : BaseFragment<FragmentReportBinding>(R.layout.fragment_report),
    TaskAdapterListener {
    private val reportViewModel: ReportViewModel by activityViewModels()
    private val taskAdapter = TaskAdapter(this)
    private val today = LocalDate.now()

    override fun initOnViewCreated() {
        binding.apply {
            reportVM = reportViewModel
            rvReport.adapter = taskAdapter
            tvSelectedDate.text = today.toString()
        }
        initView()
        initViewModelCallback()
        initClickListener()

    }

    private fun initViewModelCallback() {
        reportViewModel.taskCheck.observe(viewLifecycleOwner) {
            Log.d(TAG, "initViewModelCallback11: $it")
            if (it) {
                binding.apply {
                    tvNotice.visibility = View.GONE
                    lottie.visibility = View.GONE
                    scrollTask.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    tvNotice.visibility = View.VISIBLE
                    lottie.visibility = View.VISIBLE
                    scrollTask.visibility = View.GONE
                }
            }
        }

        reportViewModel.updateTaskSuccess.observe(viewLifecycleOwner) {
            if(it) {
                initView()
            }
        }
        
        reportViewModel.deleteTaskSuccess.observe(viewLifecycleOwner) {
            if(it) {
                initView()
            }
        }
    }

    private fun initView() {
        mainViewModel.displayBottomNav(true)

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