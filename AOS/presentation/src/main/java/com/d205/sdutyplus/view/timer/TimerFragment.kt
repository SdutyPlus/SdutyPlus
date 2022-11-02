package com.d205.sdutyplus.view.timer

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentTimerBinding
import com.d205.sdutyplus.uitls.convertTimeDateToString
import com.d205.sdutyplus.uitls.getTodayDate
import com.d205.sdutyplus.view.timer.viewmodel.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.timer


private const val TAG = "TimerFragment"
@AndroidEntryPoint
class TimerFragment : BaseFragment<FragmentTimerBinding>(R.layout.fragment_timer) {
    private val timerViewModel: TimerViewModel by viewModels()

    override fun initOnViewCreated() {
        initView()
    }

    private fun initView() {

        TodayAtView()

        initTimer()
        initObserver()
    }

    private fun setTodayAtView() {
        val todayString: String = convertTimeDateToString(getTodayDate(),"yyyy년 M월 d일")
        binding.tvToday.text = todayString
    }

    private fun initTimer() {
        binding.apply {
            ivTimer.setOnClickListener {
                when (isTimerRunning()) {
                    false -> {
                        startTimer()
                    }
                    true -> {
//                        timerViewModel.delayTimer()
//                        DelayDialog().show(requireActivity().supportFragmentManager, "DelayDialog")
                    }
                }
            }
        }
        settingTimerTime(0)
    }

    private fun isTimerRunning() :Boolean {
        return timerViewModel.isTimerRunning.value!!
    }

    private fun startTimer() {
        binding.animationView.playAnimation()
        timerViewModel.startTimer() // todo mainViewModel.user.value!!.seq
        timerViewModel.saveStartTime()
        Toast.makeText(requireActivity(), "공부 시간 측정을 시작합니다!", Toast.LENGTH_SHORT).show()
    }

    private fun initObserver() {
        timerViewModel.apply {
            timerTime.observe(viewLifecycleOwner) { time ->
                settingTimerTime(time)
            }
        }
    }

    private fun settingTimerTime(time: Int) {
        val hour = time / 60 / 60
        val min = (time / 60) % 60
        val sec = time % 60
        binding.tvTotalTime.text = String.format("%02d:%02d:%02d", hour, min, sec)
    }

}