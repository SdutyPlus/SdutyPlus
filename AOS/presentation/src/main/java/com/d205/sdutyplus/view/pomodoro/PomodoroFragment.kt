package com.d205.sdutyplus.view.pomodoro

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.CountDownTimer
import android.view.View

import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.FragmentPomodoroBinding
import com.d205.sdutyplus.utills.showToast
import com.d205.sdutyplus.view.pomodoro.viewmodel.PomodoroViewModel

const val WORKING_TIME = 25 * 60 * 1000L
const val REST_TIME = 5 * 60 * 1000L
class PomodoroFragment: BaseFragment<FragmentPomodoroBinding>(R.layout.fragment_pomodoro) {

    private val pomodoroViewModel: PomodoroViewModel by activityViewModels()

    private var currentCountDownTimer: CountDownTimer? = null
    private var isWorking: Boolean = true
    private var pomoCount: Int = 0

    override fun initOnViewCreated() {
        initView()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        requireActivity()?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(currentCountDownTimer != null) {
                    requireContext().showToast(getString(R.string.disturb_swipe_timer_alert))
                } else {
                    requireContext().showToast(getString(R.string.request_using_back_alert))
                }
            }
        })
    }

    private fun  initView() {
        initTimer()
        initBtn()
    }

    private fun initTimer() {
        updateRemainTime(WORKING_TIME)
    }

    private fun initBtn() {
        binding.apply {
            btnPomodoroStart.setOnClickListener {
                btnPomodoroStart.visibility = View.GONE
                btnPomodoroStop.visibility = View.VISIBLE

                binding.tvAnimationText.visibility = View.INVISIBLE

                binding.animationView.visibility = View.VISIBLE
                binding.animationView.setAnimation(R.raw.dont_waste_time)
                binding.animationView.playAnimation()

                binding.tvPomoCount.visibility = View.VISIBLE
                binding.tvPomoCount.text = "$pomoCount + Complete!"
                startCountDown()
            }
//
            btnPomodoroStop.setOnClickListener {
                btnPomodoroStop.visibility = View.GONE
                btnPomodoroStart.visibility = View.VISIBLE

                binding.animationView.visibility = View.INVISIBLE

                binding.tvPomoCount.visibility = View.INVISIBLE
                binding.tvAnimationText.visibility = View.VISIBLE

//
                stopCountDown()

                // todo 정말 종료 하시겠습니까? 이후 공부기록 입력 창

            }

            ivBack.setOnClickListener {
                if(currentCountDownTimer != null) {
                    requireContext().showToast(getString(R.string.disturb_swipe_timer_alert))
                    return@setOnClickListener
                }
                findNavController().popBackStack()
            }
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
        updateRemainTime(WORKING_TIME)
        currentCountDownTimer?.cancel()
        currentCountDownTimer = null

        requireContext().showToast(getString(R.string.end_pomodoro_alert))

        updateRemainTime(WORKING_TIME)
        pomoCount = 0
        isWorking = true
    }

    private fun startCountDown() {
       if(currentCountDownTimer == null) {
           val time = if(isWorking) {
               WORKING_TIME
           } else {
               REST_TIME
           }
           currentCountDownTimer = createCountDownTimer(time)
       }
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
        if(isWorking) {
            pomoCount ++
            binding.tvPomoCount.text = "$pomoCount Complete!"
            if(pomoCount < 4) {
                requireContext()!!.showToast(getString(R.string.rest_time_alert))
                updateRemainTime(REST_TIME)
                currentCountDownTimer = null
                currentCountDownTimer = createCountDownTimer(REST_TIME)

            } else {
                requireContext()!!.showToast(getString(R.string.last_rest_time_alert))
                updateRemainTime(30 * 60 * 1000)
                currentCountDownTimer = null
                currentCountDownTimer = createCountDownTimer(30 * 60 * 1000)

            }
            isWorking = !isWorking
            startCountDown()

            binding.animationView.visibility = View.VISIBLE
            binding.animationView.setAnimation(R.raw.pomodoro_rest)
            binding.animationView.playAnimation()

        } else {
            if(pomoCount < 4) {
                requireContext()!!.showToast(getString(R.string.working_time_alert))
                updateRemainTime(WORKING_TIME)

                binding.animationView.visibility = View.VISIBLE
                binding.animationView.setAnimation(R.raw.dont_waste_time)
                binding.animationView.playAnimation()

                startCountDown()
            } else {
                pomoCount = 0
                updateRemainTime(WORKING_TIME)
                binding.layoutPomodoro.setBackgroundColor(Color.parseColor("#2E2E2E"))
                requireContext()!!.showToast(getString(R.string.request_pomodoro_create_alert))

                binding.apply {
                    btnPomodoroStart.visibility = View.VISIBLE
                    btnPomodoroStop.visibility = View.GONE

                    animationView.visibility = View.INVISIBLE
                    tvAnimationText.visibility = View.VISIBLE

                    tvPomoCount.text = ""
                }
            }
            isWorking = !isWorking

        }

    }

    override fun onStop() {
        super.onStop()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

    }


}