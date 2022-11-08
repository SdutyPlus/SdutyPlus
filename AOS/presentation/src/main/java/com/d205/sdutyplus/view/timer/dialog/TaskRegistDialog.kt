package com.d205.sdutyplus.view.timer.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.d205.sdutyplus.databinding.DialogTaskRegistBinding
import com.d205.sdutyplus.uitls.getDeviceSize

import com.d205.sdutyplus.view.MainViewModel
import com.d205.sdutyplus.view.timer.viewmodel.TimerViewModel
import java.util.*

private const val TAG = "TaskDialog"

class TaskRegistDialog : DialogFragment() {
    private lateinit var binding: DialogTaskRegistBinding
    private val timerViewModel: TimerViewModel by viewModels({ requireActivity() })
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTaskRegistBinding.inflate(inflater, container, false)

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
        initView()
    }

    private fun initView() {
        binding.apply {
            btnDelete.setOnClickListener {

                timerViewModel.timerTimeReset()

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