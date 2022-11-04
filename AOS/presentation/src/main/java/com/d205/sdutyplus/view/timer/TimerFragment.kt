package com.d205.sdutyplus.view.timer

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentTimerBinding
import com.d205.sdutyplus.uitls.convertTimeDateToString
import com.d205.sdutyplus.uitls.getTodayDate
import com.d205.sdutyplus.view.timer.dialog.StopStudyConfirmDialog
import com.d205.sdutyplus.view.timer.viewmodel.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.timer


private const val TAG = "TimerFragment"
@AndroidEntryPoint
class TimerFragment : BaseFragment<FragmentTimerBinding>(R.layout.fragment_timer) {
    private val timerViewModel: TimerViewModel by activityViewModels()

    override fun initOnViewCreated() {
        initView()
    }

    private fun initView() {

        setTodayAtView()
        initTimer()
        initObserver()
    }

    private fun setTodayAtView() {
        setLocalTimeAtTodayView()
        setRemoteTimeAtTodayView()
    }

    private fun setLocalTimeAtTodayView() {
        val todayString: String = convertTimeDateToString(getTodayDate(),"yyyy년 M월 d일")
        binding.tvToday.text = todayString
    }

    private fun setRemoteTimeAtTodayView() {
        timerViewModel.getRealTime()
    }



    private fun initTimer() {
        binding.apply {
            ivTimer.setOnClickListener {
                when (isTimerRunning()) {
                    false -> {
                        startTimer() // todo 서버로 부터 시간 획득
                    }
                    true -> {
                        pauseTimer()
//                        timerViewModel.delayTimer()
//                        DelayDialog().show(requireActivity().supportFragmentManager, "DelayDialog")
                    }
                }
            }
        }
        settingTimerTime(0) // todo 서버 시간 세팅
    }

    private fun isTimerRunning() :Boolean {
        return timerViewModel.isTimerRunning.value!!
    }

    private fun startTimer() {
        binding.animationView.playAnimation()
        timerViewModel.startTimer()
        timerViewModel.saveStartTime()
        Toast.makeText(requireActivity(), "공부 시간 측정을 시작합니다!", Toast.LENGTH_SHORT).show()
    }

    private fun pauseTimer() {
        showStopStudyConfirmDialog()
    }

    private fun showStopStudyConfirmDialog() {
        StopStudyConfirmDialog(requireContext())
            .show(requireActivity().supportFragmentManager, "StopStudyConfirmDialog")
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