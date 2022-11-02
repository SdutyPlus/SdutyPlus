package com.d108.sduty.ui.main.home.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.d108.sduty.databinding.DialogPushInfoBinding
import com.d108.sduty.utils.getDeviceSize


private const val TAG ="PushInfoDialog"
class PushInfoDialog : DialogFragment() {
    private lateinit var binding: DialogPushInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPushInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnPushAll.setOnCheckedChangeListener { buttonView, isChecked ->
                when(isChecked){
                    true -> btnPushPersonal.isChecked = true
                    else -> {}
                }
            }
            btnPushPersonal.setOnCheckedChangeListener { buttonView, isChecked ->
                when(isChecked){
                    false -> btnPushAll.isChecked = false
                    else -> {}
                }
            }
            btnConfirm.setOnClickListener {
                var state = 2
                if(btnPushAll.isChecked){
                    state = 2
                }else if(btnPushPersonal.isChecked){
                    state = 1
                }else{
                    state = 0
                }
                onClickConfirm.onClick(state)
                dismiss()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    lateinit var onClickConfirm: OnClickConfirm
    interface OnClickConfirm{
        fun onClick(state: Int)
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = getDeviceSize(requireActivity()).x
        params?.width = (deviceWidth * 0.95).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}
