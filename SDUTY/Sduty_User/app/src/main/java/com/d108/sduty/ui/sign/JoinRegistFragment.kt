package com.d108.sduty.ui.sign

import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d108.sduty.common.COMMON_JOIN
import com.d108.sduty.common.KAKAO_JOIN
import com.d108.sduty.common.NAVER_JOIN
import com.d108.sduty.databinding.FragmentJoinRegistBinding
import com.d108.sduty.model.dto.User
import com.d108.sduty.ui.sign.viewmodel.JoinViewModel
import com.d108.sduty.utils.showToast
import java.util.regex.Pattern

//회원가입 - 정보입력 / 이름, 이메일, 아이디, 비밀번호, 비밀번호 확인, 휴대폰 번호, 인증
private const val TAG ="JoinRegistFragment"
class JoinRegistFragment : Fragment() {
    private lateinit var binding: FragmentJoinRegistBinding
    private val viewModel: JoinViewModel by viewModels()
    private val args: JoinRegistFragmentArgs by navArgs()

    private val mailList = listOf("gmail.com", "naver.com", "daum.net", "직접 입력")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJoinRegistBinding.inflate(inflater, container, false)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initView()
    }

    private fun initViewModel() {
        viewModel.apply {
            setToken(args.token)
            when(args.route){
                KAKAO_JOIN -> {                    
                    kakaoUserInfo()
                }
                NAVER_JOIN -> {                    
                    naverUserInfo()
                }
            }
            setJoinFlag(args.route)
            socialUser.observe(viewLifecycleOwner){
                binding.user = it
            }
            isJoinSucced.observe(viewLifecycleOwner){
                when(it){
                    true -> {
                        requireContext().showToast("회원 가입이 완료되었습니다.")
                    }
                    else -> {}
                }
                findNavController().navigate(JoinRegistFragmentDirections.actionJoinRegistFragmentToLoginFragment())
            }
            isSucceedSendAuthInfo.observe(viewLifecycleOwner){
                binding.vm = viewModel
            }
            isSucceedAuth.observe(viewLifecycleOwner){
                binding.vm = viewModel
            }
        }
    }

    private fun initView() {
        binding.apply {
            lifecycleOwner = this@JoinRegistFragment
            vm = viewModel
            btnSendSms.setOnClickListener {
                val tel = "${etPhoneFront.text}${etPhoneEnd.text}"
                if(tel.length == 11)
                    viewModel.sendOTP(tel)
                else{
                    requireContext().showToast("정확한 번호를 입력해 주세요")
                }
            }
            btnAuthCode.setOnClickListener {
                    viewModel.checkOTP(etAuthCode.text.toString())
            }
            btnJoin.setOnClickListener {
                join()
            }
            etPw.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    if(s != null){
                        viewModel.checkPWLength(binding.etPw.text.toString())
                    }
                }
            })
            etPwConfirm.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    viewModel.checkPW(etPw.text.toString(), etPwConfirm.text.toString())
                }
            })
            etId.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    viewModel.getUsedId(binding.etId.text.toString())
                }
            })
            etName.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    viewModel.checkName(binding.etName.text.toString())
                }
            })
            spinnerEmail.apply {
                adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mailList)
                onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if(position == mailList.size - 1){
                            viewModel.setSelfInput()
                        }
                    }
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            }
            var filter = arrayOf(InputFilter{src, start, end, dst, dstart, dend ->
                val ps = Pattern.compile("^[a-zA-Z0-9]+\$")
                if(!ps.matcher(src).matches()){
                    return@InputFilter ""
                }else{
                    return@InputFilter null
                }
            }, InputFilter.LengthFilter(15))
            etId.filters = filter

            filter = arrayOf(InputFilter{src, start, end, dst, dstart, dend ->
                val ps = Pattern.compile("^[0-9]+\$")
                if(!ps.matcher(src).matches()){
                    return@InputFilter ""
                }else{
                    return@InputFilter null
                }
            }, InputFilter.LengthFilter(8))

            etPhoneEnd.filters = filter

            filter = arrayOf(InputFilter{src, start, end, dst, dstart, dend ->
                val ps = Pattern.compile("^[a-zA-Zㄱ-ㅎ가-힣]+\$")
                if(!ps.matcher(src).matches()){
                    return@InputFilter ""
                }else{
                    return@InputFilter null
                }
            }, InputFilter.LengthFilter(15))
            etName.filters = filter

            filter = arrayOf(InputFilter{src, start, end, dst, dstart, dend ->
                val ps = Pattern.compile("^[a-zA-Z0-9!@#$%^&*]+\$")
                if(!ps.matcher(src).matches()){
                    return@InputFilter ""
                }else{
                    return@InputFilter null
                }
            }, InputFilter.LengthFilter(20))

            etPw.filters = filter
            etPwConfirm.filters = filter

        }
    }

    private fun join(){
        binding.apply {
            val id = etId.text.toString()
            val pw = etPw.text.toString()
            val name = etName.text.toString()
            val tel = "${etPhoneFront.text}${etPhoneEnd.text}"
            val email = if(spinnerEmail.selectedItemPosition == mailList.size - 1 || viewModel.socialUser.value != null){
                "${etEmail.text}@${etEmailEnd.text}"
            }else{
                "${etEmail.text}@${spinnerEmail.selectedItem}"
            }
            if(args.route == COMMON_JOIN) {
                if(id.isEmpty() || pw.isEmpty() || name.isEmpty() || tel.isEmpty() || email.isEmpty() || tel.length < 11){
                    requireContext().showToast("모든 항목을 입력해 주세요.")
                }else{
                    viewModel.join(User(id, pw, name, tel, email))
                }
            }else{
                if(name.isEmpty() || tel.isEmpty() || email.isEmpty()){
                    requireContext().showToast("모든 항목을 입력해 주세요.")
                }else{
                    viewModel.join(User(email, email, name, tel, email))
                }
            }
        }
    }

}