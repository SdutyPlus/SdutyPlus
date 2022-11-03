package com.d205.sdutyplus.view.report

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentReportBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ReportFragment : BaseFragment<FragmentReportBinding>(R.layout.fragment_report) {
    private val reportViewModel by viewModels<ReportViewModel>()
    private val today = LocalDate.now()

    override fun initOnViewCreated() {
        lottie()
        initClickListener()
        initView()
    }

    private fun initView() {
        binding.tvSelectedDate.text = today.toString()
    }


    private fun initClickListener() {
        binding.ivCalendarCall.setOnClickListener {
            val dialog = CalendarBottomSheetFragment()
            dialog.show(parentFragmentManager, "BottomSheet")
            // test
            // reportViewModel.get("2000-22-22")
        }
    }

    private fun lottie() {
        binding.lottie.playAnimation()
    }

}