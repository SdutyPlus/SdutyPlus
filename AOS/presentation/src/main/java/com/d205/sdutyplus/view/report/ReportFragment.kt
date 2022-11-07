package com.d205.sdutyplus.view.report

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.d205.domain.model.report.Task
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentReportBinding
import com.d205.sdutyplus.uitls.showToast
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
        initClickListener()
    }

    private fun initView() {
        reportViewModel.getReportTotalTime(binding.tvSelectedDate.text.toString())
        reportViewModel.getTaskList(binding.tvSelectedDate.text.toString())

//        reportViewModel.today.observe(viewLifecycleOwner) {
//            Log.d(TAG, "initView: $it")
//            binding.tvSelectedDate.text = it
//        }
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
        context?.showToast("asdas")
        Log.d(TAG, "onTaskClicked: toastmessage")
    }

}