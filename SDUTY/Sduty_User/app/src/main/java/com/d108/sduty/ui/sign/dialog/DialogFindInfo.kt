package com.d108.sduty.ui.sign.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.d108.sduty.common.FIND_ID
import com.d108.sduty.common.FIND_PW
import com.d108.sduty.databinding.DialogFindInfoBinding
import com.d108.sduty.model.dto.User
import com.d108.sduty.ui.sign.viewmodel.FindInfoViewModel
import com.d108.sduty.ui.sign.viewmodel.JoinViewModel
import com.d108.sduty.utils.showToast

private const val TAG ="DialogFindInfo"
class DialogFindInfo(val mContext: Context) : DialogFragment() {
    private lateinit var binding: DialogFindInfoBinding
    private val viewModel: FindInfoViewModel by viewModels()
    private val joinViewModel: JoinViewModel by viewModels()
    private var flag = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFindInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flag = requireArguments().getInt("flag")

        initView()
        initViewModel()
    }

    private fun initViewModel(){
        viewModel.setFlag(flag)
        viewModel.isSucceedFindId.observe(viewLifecycleOwner){
            if(it == true){
                requireContext().showToast("입력하신 번호로 아이디가 전송되었습니다.")
                dismiss()
            }else{
                requireContext().showToast("입력하신 번호로 가입된 아이디가 없습니다.")
            }
        }
        viewModel.isSucceedChangePw.observe(viewLifecycleOwner){
            if(it == true){
                requireContext().showToast("비밀번호가 변경되었습니다.")
                dismiss()
            }else{
                requireContext().showToast("잘못된 정보입니다")
            }
        }
    }

    private fun initView(){
        binding.apply {
            lifecycleOwner = this@DialogFindInfo
            vm = viewModel
            joinVM = joinViewModel

            btnConfirm.setOnClickListener {
                when(flag){
                    FIND_ID ->{
                        if(etName.text.isEmpty() || etPhone.text.isEmpty() || etPhone.text.length != 11){
                            requireContext().showToast("올바른 정보를 입력해 주세요")
                        }else{
                            viewModel.findId(etName.text.toString(), etPhone.text.toString())
                        }
                    }
                    FIND_PW ->{
                        if(etPhone.text.isEmpty() || etPhone.text.length != 11){
                            requireContext().showToast("올바른 번호를 입력해 주세요")
                        }else{
                            joinViewModel.sendOTP(etPhone.text.toString())
                        }
                    }

                }
            }
            btnAuthCode.setOnClickListener {
                joinViewModel.checkOTP(etAuthCode.text.toString())
            }
            btnChangePw.setOnClickListener {
                val id = etId.text.toString()
                val pw = etPw.text.toString()
                if(id.isEmpty() || pw.isEmpty()){
                    requireContext().showToast("모든 항목을 입력해 주세요")
                }else{
                    viewModel.changePw(User(id, pw))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        resizeDialog()
    }

    private fun resizeDialog() {
        val windowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceHeight * 0.95).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}