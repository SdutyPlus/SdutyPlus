package com.d108.sduty.ui.main.timer.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.d108.sduty.databinding.DialogConfirmBinding
import com.d108.sduty.ui.main.timer.viewmodel.TimerViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.getDeviceSize

class ConfirmDialog : DialogFragment() {
    private lateinit var binding: DialogConfirmBinding
    private val timerViewModel : TimerViewModel by viewModels({requireActivity()})
    private val mainViewModel : MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogConfirmBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 여백 터치 시 다이얼로그 종료 방지
        isCancelable = false

        val action = arguments?.getString("Action", "RemoveTimer")

        when(action){
            // 시간 측정 기록 삭제
            "RemoveTimer" -> {
                removeTimer()
            }
            // 서버에 Task 삭제 요청
            "RemoveTask" -> {
                removeTask()
            }
            // 경고
            "Error" -> {
                errorMessage()
            }
        }
    }

    private fun removeTimer() {
        binding.apply {
            btnRemove.setOnClickListener {
                // TaskRegistDialog 종료
                val fragment = requireActivity().supportFragmentManager.findFragmentByTag("TaskRegistDialog")
                requireActivity().supportFragmentManager.beginTransaction().remove(fragment!!).commit()

                // ConfirmDialog 종료
                dismiss()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun removeTask() {
        val position = arguments?.getInt("Position", 0)

        binding.apply {
            btnRemove.setOnClickListener {
                timerViewModel.removeTask(mainViewModel.user.value!!.seq, position!!)
                dismiss()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun errorMessage() {
        val message = arguments?.getString("Message", "")
        binding.apply {
            tvWarningMessage.text = message
            btnRemove.visibility = View.GONE
            btnCancel.visibility = View.GONE
            btnDone.visibility = View.VISIBLE
            btnDone.setOnClickListener {
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