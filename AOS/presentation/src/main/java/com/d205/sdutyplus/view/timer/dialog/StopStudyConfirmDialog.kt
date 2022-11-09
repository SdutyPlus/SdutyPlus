package com.d205.sdutyplus.view.timer.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.d205.sdutyplus.databinding.DialogStopStudyConfirmBinding
import com.d205.sdutyplus.uitls.getDeviceSize
import com.d205.sdutyplus.view.timer.viewmodel.TimerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.concurrent.timer


private const val TAG = "StopStudyConfirmDialog"
@AndroidEntryPoint
class StopStudyConfirmDialog(context: Context) : DialogFragment() {
    private lateinit var binding: DialogStopStudyConfirmBinding

    private val timerViewModel: TimerViewModel by activityViewModels()
//    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogStopStudyConfirmBinding.inflate(inflater, container, false)

        initDialogWindow()

        return binding.root
    }

    private fun initDialogWindow() {
        dialog?.window?.apply{
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }
        // 여백 클릭 종료 불가 설정
        isCancelable = false
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startResumeStudyCountDown()
        initObserver()
        initView()
    }

    private fun initObserver() {

        timerViewModel.apply {
            timerTime.observe(viewLifecycleOwner) { currentTimerTime ->
                setTimerTime(currentTimerTime)
            }

            resumeCountDown.observe(viewLifecycleOwner) { resumeCountDown ->
                if(isCountDownOn()) {
                    binding.tvCountdown.text = "측정을 이어서 하려면 \n[${resumeCountDown}]초 이내에 클릭하세요!"
                } else {
                    binding.tvCountdown.text = "이어할 수 있는 시간이 \n 지났습니다!"
                    binding.btnContinue.visibility = View.GONE // todo 종료와 같은 기능
                    stopTimer()
                }

            }
        }

//        timerViewModel.timer.observe(viewLifecycleOwner) { time ->
//            val hour = time / 60 / 60
//            val min = (time / 60) % 60
//            val sec = time % 60
//            binding.tvTimer.text = String.format("%02d:%02d:%02d", hour, min, sec)
//        }
//
//        timerViewModel.resumeCountDown.observe(viewLifecycleOwner) { countDown ->
//            // 20초가 경과하면 종료
//            if (delayTime == 20) {
//                TaskRegistDialog().apply {
//                    arguments = Bundle().apply {
//                        putString("Action", "Add")
//                    }
//                }.show(
//                    this@StopStudyConfirmDialog.requireActivity().supportFragmentManager,
//                    "TaskRegistDialog"
//                )
//
//                timerViewModel.resetDelayTimer()
//                timerViewModel.stopTimer(mainViewModel.user.value!!.seq)
//                dismiss()
//            }
//            setResumeStudy
//            binding.tvCountdown.text = "측정을 이어서 하려면 \n[${20 - delayTime}]초 이내에 클릭하세요"
//        }
    }

    private fun isCountDownOn(): Boolean {
        return timerViewModel.resumeCountDown.value!! > 0
    }

    private fun setTimerTime(time: Int) {
        val hour = time / 60 / 60
        val min = (time / 60) % 60
        val sec = time % 60
        binding.tvTimer.text = String.format("%02d:%02d:%02d", hour, min, sec)
    }

    private fun resetResumeStudyCountDown() {
        timerViewModel.resumeCountDownReset()
    }

    private fun startResumeStudyCountDown() {
        timerViewModel.startResumeCountDown()
    }



    
    
    private fun initView() {
        binding.apply {
            btnContinue.setOnClickListener {
                dismiss()
            }

            btnFinish.setOnClickListener {
                TaskRegistDialog().apply {
                    arguments = Bundle().apply {
                        putString("Action", "Add")
                    }
                    show(
                        this@StopStudyConfirmDialog.requireActivity().supportFragmentManager,
                        "TaskRegistDialog"
                    )
                }
//
//                Toast.makeText(requireActivity(), "측정을 종료 하였습니다!", Toast.LENGTH_SHORT).show()
//
//                timerViewModel.resetDelayTimer()
//                timerViewModel.stopTimer(mainViewModel.user.value!!.seq)
                /*
                timerViewModel.stopTimer()
                timerViewModel.timerTimeReset()
                 */
                timerViewModel.stopTimer()
                dismiss()

            }

        }
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes

        // 앱을 실행한 디바이스의 가로, 세로 크기를 가져온다.
        val deviceWidth = getDeviceSize(requireActivity()).x

        // 다이얼로그 크기를 디바이스 가로의 90%로 설정한다.
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }

    override fun onDestroy() {
        super.onDestroy()

        resetResumeStudyCountDown()
    }
}
