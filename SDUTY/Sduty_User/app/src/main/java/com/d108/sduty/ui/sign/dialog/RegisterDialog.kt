package com.d108.sduty.ui.sign.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.d108.sduty.R
import com.d108.sduty.common.COMMON_JOIN
import com.d108.sduty.databinding.DialogRegisterBinding
import com.d108.sduty.ui.sign.viewmodel.JoinViewModel
import com.d108.sduty.utils.showToast

class RegisterDialog() : DialogFragment() {
    //    private val dialog = Dialog(context)
    private lateinit var binding: DialogRegisterBinding
    private val viewModel: JoinViewModel by viewModels()

    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener) {
        onClickListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // set dialog - no title
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

        binding = DialogRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDialog()
        initView()
        initViewModel()
    }

    private fun initView() {
        binding.apply {
            btnJoinId.setOnClickListener {
                onClickListener.onClicked(COMMON_JOIN)
            }
//            btnJoinKakao.setOnClickListener {
//                onClickListener.onClicked(KAKAO_JOIN)
//            }
//            btnJoinNaver.setOnClickListener {
//                onClickListener.onClicked(NAVER_JOIN)
//            }
        }
    }

    private fun initViewModel(){
        viewModel.isJoinSucced.observe(viewLifecycleOwner){
            when(it) {
                true -> requireContext().showToast("가입이 완료되었습니다. 로그인해 주세요")
                else -> {}
            }
        }
    }

    fun showDialog() {
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE)) // TRANSPARENT?

        dialog?.window?.attributes?.windowAnimations = R.style.RegisterDialogAnimation
        dialog?.window?.setGravity(Gravity.BOTTOM)

        dialog?.setCanceledOnTouchOutside(true)
        dialog?.setCancelable(true)
        dialog?.show()
    }

    interface OnDialogClickListener{
        fun onClicked(joinType: Int)
    }
}