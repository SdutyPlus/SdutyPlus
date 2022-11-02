package com.d108.sduty.ui.main.timer

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d108.sduty.databinding.FragmentTimerBinding
import com.d108.sduty.ui.main.timer.dialog.DelayDialog
import com.d108.sduty.ui.main.timer.viewmodel.TimerViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.convertTimeDateToString
import com.d108.sduty.utils.safeNavigate
import com.d108.sduty.utils.showToast
import java.util.*

private const val TAG = "TimerFragment"
class TimerFragment : Fragment() {
    private lateinit var binding: FragmentTimerBinding

    private val mainViewModel : MainViewModel by activityViewModels()
    private val timerViewModel : TimerViewModel by viewModels({requireActivity()})

    private lateinit var today : String

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel.displayBottomNav(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰모델 초기화한다.
        initViewModel()

        // 첫 화면을 설정한다.
        initView()
    }

    private fun initViewModel(){
        binding.apply {
            lifecycleOwner = this@TimerFragment
            timerVM = timerViewModel
        }

        timerViewModel.apply {
            // 토스트 메시지 출력 요청
            isInsertedTask.observe(viewLifecycleOwner) { succeeded ->
                if (succeeded) {
                    requireContext().showToast("등록이 완료되었습니다.")
                    resetLiveData("isInsertedTask")
                }
            }

            isErredConnection.observe(viewLifecycleOwner) { erred ->
                if (erred) {
                    requireContext().showToast("서버와 연결하는데 실패했습니다.")
                    resetLiveData("isErredConnection")
                }
            }

            // 하루 공부한 시간
            timerViewModel.report.observe(viewLifecycleOwner) { report ->
                binding.tvTotalTime.text = report.totalTime
            }

            // 타이머
            timer.observe(viewLifecycleOwner) { time ->
                val hour = time / 60 / 60
                val min = (time / 60) % 60
                val sec = time % 60
                binding.tvTimer.text = String.format("%02d:%02d:%02d", hour, min, sec)
            }
        }
    }

    // 화면 초기화
    private fun initView() {
        // 오늘 날짜
        today = convertTimeDateToString(Date(System.currentTimeMillis()), "yyyy년 M월 d일")

        binding.apply {
//            // 날짜 선택
//            commonSelectedDate.setOnClickListener {
//                showDatePicker()
//            }

            // 공부 시작, 종료
            ivTimer.setOnClickListener {
                when (timerViewModel.isRunningTimer.value as Boolean) {
                    false -> {
                        animationView.playAnimation()
                        timerViewModel.startTimer(mainViewModel.user.value!!.seq)
                        timerViewModel.saveTime()
                        Toast.makeText(requireActivity(), "공부 시간 측정을 시작합니다!", Toast.LENGTH_SHORT).show()
                    }
                    true -> {
                        timerViewModel.delayTimer()
                        DelayDialog().show(requireActivity().supportFragmentManager, "DelayDialog")
                    }
                }
            }

//            // 오늘 날짜로 돌아가기
//            btnReturnToday.setOnClickListener {
//                commonSelectedDate.text = today
//                tvTimer.visibility = View.VISIBLE
//                ivTimer.visibility = View.VISIBLE
//                btnReturnToday.visibility = View.INVISIBLE
//                timerViewModel.selectDate(today)
//            }

            // 리포트로 이동
            fabReport.setOnClickListener {
                findNavController().safeNavigate(TimerFragmentDirections.actionTimerFragmentToReportFragment())
            }

            // 첫 화면은 오늘 날짜로 설정
            tvToday.text = today
            timerViewModel.selectDate(mainViewModel.user.value!!.seq, today)
            timerViewModel.restoreTime(mainViewModel.user.value!!.seq)
        }
    }

//    private fun showDatePicker(){
//        val cal = Calendar.getInstance()
//        // 날짜 선택 후 동작할 리스너
//        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
//            val selectedDate = "${year}년 ${month + 1}월 ${day}일"
//            binding.commonSelectedDate.text = selectedDate
//            timerViewModel.selectDate(selectedDate)
//
//            when(selectedDate == today){
//                true -> { // 오늘
//                    binding.apply {
//                        tvTimer.visibility = View.VISIBLE
//                        ivTimer.visibility = View.VISIBLE
//                        btnReturnToday.visibility = View.GONE
//                    }
//                }
//                false -> { // 다른 날
//                    binding.apply {
//                        tvTimer.visibility = View.GONE
//                        ivTimer.visibility = View.INVISIBLE
//                        btnReturnToday.text = "오늘($today) 로 돌아가기"
//                        btnReturnToday.visibility = View.VISIBLE
//                    }
//                }
//            }
//        }
//        // 캘린더 다이얼로그 출력
//        DatePickerDialog(requireActivity(), dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
//    }
}
