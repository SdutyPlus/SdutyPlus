package com.d205.sdutyplus.view.pomodoro

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.view.View
import android.widget.SeekBar
import androidx.databinding.adapters.SeekBarBindingAdapter.setOnSeekBarChangeListener
import androidx.fragment.app.activityViewModels
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentPomodoroBinding
import com.d205.sdutyplus.uitls.showToast
import com.d205.sdutyplus.view.pomodoro.viewmodel.PomodoroViewModel

class PomodoroFragment: BaseFragment<FragmentPomodoroBinding>(R.layout.fragment_pomodoro) {
    private val pomodoroViewModel: PomodoroViewModel by activityViewModels()
    private var currentCountDownTimer: CountDownTimer? = null
    private var isWorking: Boolean = true

    override fun initOnViewCreated() {
        initView()
    }

    private fun  initView() {
        initTimer()
        initBtn()
//        initSeekBar()
    }

    private fun initTimer() {
        createCountDownTimer(25 * 60 * 1000L)
        updateRemainTime(25 * 60 * 1000)
    }

    private fun initBtn() {
        binding.apply {
            btnPomodoroStart.setOnClickListener {
                btnPomodoroStart.visibility = View.GONE
                btnPomodoroPause.visibility = View.VISIBLE

                startCountDown()
            }
//
            btnPomodoroPause.setOnClickListener {
//                btnPomodoroPause.visibility = View.GONE
//                btnPomodoroStart.visibility = View.VISIBLE
//
//                stopCountDown()
            }

//            ivBack.setOnClickListener {
//                if(currentCountDownTimer)
//            }
        }
    }



    @SuppressLint("SetTextI18n")
    private fun updateRemainTime(remainMillis: Long) {
        val remainSeconds = remainMillis / 1000

        binding.apply {
            tvMinute.text = "%02d'".format(remainSeconds / 60)
            tvSeconds.text = "%02d".format(remainSeconds % 60)
        }
    }

    private fun stopCountDown() {
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null
    }

    private fun startCountDown() {
//        currentCountDownTimer = createCountDownTimer(binding.seekBar.progress * 60 * 1000L)
        currentCountDownTimer?.start()
    }

    private fun createCountDownTimer(initialMillis: Long) =
        object : CountDownTimer(initialMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainTime(millisUntilFinished)
//                updateSeekBar(millisUntilFinished)
            }

            override fun onFinish() {
                completeCountDown()
            }
        }

    private fun completeCountDown() {
        updateRemainTime(25)
        binding.apply {
             btnPomodoroPause.visibility = View.GONE
             btnPomodoroStart.visibility = View.VISIBLE
        }
        if(isWorking) {
            requireContext()!!.showToast("완료하였습니다! \n휴식 시간이에요!")
            updateRemainTime(5 * 60 * 1000)
            isWorking = !isWorking
            binding.layoutPomodoro.setBackgroundResource(R.drawable.bg_pomodoro_rest)
        } else {
            requireContext()!!.showToast("진행 시간이에요!")
            updateRemainTime(25 * 60 * 1000)
            isWorking = !isWorking
            binding.layoutPomodoro.setBackgroundResource(R.drawable.bg_pomodoro_work)
        }

    }


}