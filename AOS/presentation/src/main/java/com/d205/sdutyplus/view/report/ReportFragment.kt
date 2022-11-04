package com.d205.sdutyplus.view.report

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.d205.domain.model.report.Task
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentReportBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ReportFragment : BaseFragment<FragmentReportBinding>(R.layout.fragment_report),
    ReportAdapterListener {
    private val reportViewModel by viewModels<ReportViewModel>()
    private val reportAdapter = ReportAdapter(this)
    private val today = LocalDate.now()

    override fun initOnViewCreated() {
        binding.apply {
            reportVM = reportViewModel
            rvReport.adapter = reportAdapter
        }
        lottie()
        initView()
        initClickListener()
    }

    private fun initView() {
        binding.tvSelectedDate.text = today.toString()

        // test
        // reportViewModel.get("2000-22-22")
    }


    private fun initClickListener() {
        binding.ivCalendarCall.setOnClickListener {
            val dialog = CalendarBottomSheetFragment()
            dialog.show(parentFragmentManager, "BottomSheet")
        }
    }

    private fun lottie() {
        binding.lottie.playAnimation()
    }

    override fun onItemClicked(task: Task) {
        TODO("Not yet implemented")
    }

}