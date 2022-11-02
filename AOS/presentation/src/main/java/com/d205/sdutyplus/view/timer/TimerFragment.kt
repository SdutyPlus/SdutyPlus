package com.d205.sdutyplus.view.timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentTimerBinding
import com.d205.sdutyplus.uitls.convertTimeDateToString
import com.d205.sdutyplus.uitls.getTodayDate
import com.d205.sdutyplus.view.MainViewModel
import com.d205.sdutyplus.view.timer.viewmodel.TimerViewModel
import kotlin.concurrent.timer


private const val TAG = "TimerFragment"
class TimerFragment : BaseFragment<FragmentTimerBinding>(R.layout.fragment_timer) {

    override fun initOnViewCreated() {
        initView()
    }

    private fun initView() {
        val todayString: String = convertTimeDateToString(getTodayDate())

        initTimer()
    }

    private val timerViewModel: TimerViewModel by viewModels()
    private fun initTimer() {

        binding.apply {
            ivTimer.setOnClickListener {
                when (timerViewModel.isRunningTimer.value as Boolean) {
                    false -> {
                        animationSettingOnFalse()
                    }
                    true -> {
                        timerViewModel.delayTimer()
                        DelayDialog().show(requireActivity().supportFragmentManager, "DelayDialog")
                    }
                }
            }
        }

    }

    private fun animationSettingOnFalse() {
        binding.animationView.playAnimation()
        timerViewModel.startTimer(mainViewModel.user.value!!.seq)
        timerViewModel.saveTime()
        Toast.makeText(requireActivity(), "공부 시간 측정을 시작합니다!", Toast.LENGTH_SHORT).show()
    }
}