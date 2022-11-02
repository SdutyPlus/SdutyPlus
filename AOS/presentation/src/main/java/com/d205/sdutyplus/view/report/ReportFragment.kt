package com.d205.sdutyplus.view.report

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentReportBinding

@RequiresApi(Build.VERSION_CODES.O)
class ReportFragment : BaseFragment<FragmentReportBinding>(R.layout.fragment_report) {

    override fun initOnViewCreated() {
        lottie()
        initClickListener()
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

}