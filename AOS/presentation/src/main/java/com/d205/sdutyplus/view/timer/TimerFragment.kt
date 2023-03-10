package com.d205.sdutyplus.view.timer

import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentTimerBinding
import com.d205.sdutyplus.utils.showToast
import com.d205.sdutyplus.view.timer.dialog.StopStudyConfirmDialog
import com.d205.sdutyplus.view.timer.viewmodel.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "TimerFragment"
@AndroidEntryPoint
class TimerFragment : BaseFragment<FragmentTimerBinding>(R.layout.fragment_timer) {
    private val timerViewModel: TimerViewModel by activityViewModels()

    override fun initOnViewCreated() {
        initView()
    }

    private fun initView() {
        displayBottomNav(true)
        setTodayInfo()
        initTimer()
        initObserver()
        initBtn()
    }

    private fun setTodayInfo() {
        setTodayAtView()
        setTodayTotalStudyTime() // todo 서버로 부터 오늘 총 공부 시간을 가져와 세팅해 준다.
    }

    private fun setTodayAtView() {
        timerViewModel.getCurrentTime()
    }

    private fun setTodayTotalStudyTime() {
        binding.tvTotalTime.text = getString(R.string.total_time_initial)
        timerViewModel.getTodayTotalStudyTime()
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
        timerViewModel.startTimer()
        timerViewModel.saveStartTime()
        Toast.makeText(requireActivity(), getString(R.string.timer_start_alert), Toast.LENGTH_SHORT).show()
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

            currentTime.observe(viewLifecycleOwner) { currentTime ->
                binding.tvToday.text = currentTime
            }

            isTimerRunning.observe(viewLifecycleOwner) { isTimerRunning ->
                if(isTimerRunning) {
                    activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    binding.ivTimer.setImageResource(R.drawable.ic_stop) // todo refactor
                    binding.animationView.visibility = View.VISIBLE
                    binding.animationView.playAnimation()
                } else {
                    activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                    binding.ivTimer.setImageResource(R.drawable.ic_play)
                    binding.animationView.visibility = View.GONE
                }

            }

            todayTotalStudyTime.observe(viewLifecycleOwner) { todayTotalStudyTime ->
                if(!timerViewModel.isTimerRunning.value!!) {
                    binding.tvTotalTime.text = todayTotalStudyTime
                }
            }

            updatedTotalTime.observe(viewLifecycleOwner) { updatedTotalTime ->
                binding.tvTotalTime.text = updatedTotalTime
            }

            addTaskCallBack.observe(viewLifecycleOwner) { isSuccess ->
                if(isSuccess == 200) {
                    setTodayTotalStudyTime()
                }
            }
        }
    }

    private fun initBtn() {
        binding.ivPomodoro.setOnClickListener {
            if(timerViewModel.isTimerRunning.value == true) {
                requireContext().showToast(getString(R.string.disturb_swipe_timer_alert))
                return@setOnClickListener
            }

            findNavController().navigate(TimerFragmentDirections.actionTimerFragmentToPomodoroFragment())
        }
    }

    private fun settingTimerTime(time: Int) {
        val hour = time / 60 / 60
        val min = (time / 60) % 60
        val sec = time % 60
        binding.tvTimer.text = String.format("%02d:%02d:%02d", hour, min, sec)
    }

    override fun onResume() {
        super.onResume()

        if(!timerViewModel.isTimerRunning.value!!) {
            setTodayInfo()
        }
    }

}