package com.d108.sduty.ui.main.mypage.dialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.DialogFragment
import com.d108.sduty.databinding.DialogAccessibilityBinding
import com.d108.sduty.utils.getDeviceSize
import com.d108.sduty.utils.navigateBack

class AccessibilityDialog : DialogFragment() {
    private lateinit var binding: DialogAccessibilityBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAccessibilityBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        initView()
    }

    private fun initView() {
        binding.apply {
            btnCancel.setOnClickListener {
                dismiss()
                navigateBack(requireActivity())
            }
            btnContinue.setOnClickListener {
                requireContext().startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
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