package com.d108.sduty.ui.main.home.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.d108.sduty.common.FLAG_BLOCK
import com.d108.sduty.common.FLAG_DELETE
import com.d108.sduty.common.FLAG_REPORT
import com.d108.sduty.databinding.DialogBlockBinding
import com.d108.sduty.utils.getDeviceSize
import com.d108.sduty.utils.showToast

private const val TAG ="BlockDialog"
class BlockDialog(private val flag: Int) : DialogFragment() {
    private lateinit var binding: DialogBlockBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogBlockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

    }

    private fun initView(){
        binding.apply {
            when (flag) {
                FLAG_REPORT -> {
                    tvWarningMessage.text = "정말로 신고하시겠어요?\n관리자가 검토 후 삭제해 드릴게요!"
                    btnConfirm.text = "신고"
                    constReport.visibility = View.VISIBLE
                }
                FLAG_BLOCK -> {
                    tvWarningMessage.text = "정말로 차단하시겠어요?\n차단한 게시물은 다시 볼 수 없어요!"
                    btnConfirm.text = "차단"
                    constReport.visibility = View.GONE
                }
                FLAG_DELETE -> {
                    tvWarningMessage.text = "정말로 삭제하시겠어요?\n삭제한 게시물은 복구 할 수 없어요!"
                    btnConfirm.text = "삭제"
                    constReport.visibility = View.GONE
                }
            }

            btnConfirm.setOnClickListener {
                when (flag) {
                    FLAG_REPORT -> {
                        onClickConfirmListener.onClick()
                        requireContext().showToast("신고되었습니다.")
                        dismiss()
                    }
                    FLAG_BLOCK -> {
                        onClickConfirmListener.onClick()
                        requireContext().showToast("차단되었습니다.")
                        dismiss()
                    }
                    FLAG_DELETE -> {
                        onClickConfirmListener.onClick()
                        requireContext().showToast("삭제되었습니다.")
                        dismiss()
                    }
                }
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }

    lateinit var onClickConfirmListener: OnClickConfirmListener
    interface OnClickConfirmListener{
        fun onClick()
    }


    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = getDeviceSize(requireActivity()).x
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}
