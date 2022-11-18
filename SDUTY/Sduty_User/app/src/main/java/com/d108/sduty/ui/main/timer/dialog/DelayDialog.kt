package com.d108.sduty.ui.main.timer.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.d108.sduty.databinding.DialogDelayBinding
import com.d108.sduty.ui.main.timer.viewmodel.TimerViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.getDeviceSize


private const val TAG = "ReportDialog"

class DelayDialog : DialogFragment() {
    private lateinit var binding: DialogDelayBinding

    private val timerViewModel: TimerViewModel by viewModels({ requireActivity() })
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogDelayBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 여백 터치 시 다이얼로그 종료 방지
        isCancelable = false

        initViewModel()

        initView()
    }

    private fun initViewModel() {
        timerViewModel.timer.observe(viewLifecycleOwner) { time ->
            val hour = time / 60 / 60
            val min = (time / 60) % 60
            val sec = time % 60
            binding.tvTimer.text = String.format("%02d:%02d:%02d", hour, min, sec)
        }

        timerViewModel.delayTime.observe(viewLifecycleOwner) { delayTime ->
            // 20초가 경과하면 종료
            if (delayTime == 20) {
                TaskRegistDialog().apply {
                    arguments = Bundle().apply {
                        putString("Action", "Add")
                    }
                }.show(
                    this@DelayDialog.requireActivity().supportFragmentManager,
                    "TaskRegistDialog"
                )

                timerViewModel.resetDelayTimer()
                timerViewModel.stopTimer(mainViewModel.user.value!!.seq)
                dismiss()
            }
            binding.tvCountdown.text = "측정을 이어서 하려면 \n[${20 - delayTime}]초 이내에 클릭하세요"
        }
    }

    private fun initView() {
        binding.apply {
            btnContinue.setOnClickListener {
                timerViewModel.resetDelayTimer()
                dismiss()
            }

            btnFinish.setOnClickListener {
                TaskRegistDialog().apply {
                    arguments = Bundle().apply {
                        putString("Action", "Add")
                    }
                    show(
                        this@DelayDialog.requireActivity().supportFragmentManager,
                        "TaskRegistDialog"
                    )
                }

                Toast.makeText(requireActivity(), "측정을 종료 하였습니다!", Toast.LENGTH_SHORT).show()

                timerViewModel.resetDelayTimer()
                timerViewModel.stopTimer(mainViewModel.user.value!!.seq)
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
}
