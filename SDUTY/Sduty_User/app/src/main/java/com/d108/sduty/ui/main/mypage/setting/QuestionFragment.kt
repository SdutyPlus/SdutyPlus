package com.d108.sduty.ui.main.mypage.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.d108.sduty.databinding.FragmentQuestionBinding
import com.d108.sduty.model.dto.Qna
import com.d108.sduty.ui.main.mypage.setting.viewmodel.SettingViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.showToast

// 1:1 문의 - 문의 내역 조회, 1:1 문의 등록
private const val TAG ="QuestionFragment"
class QuestionFragment : Fragment() {
    private lateinit var binding: FragmentQuestionBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val viewModel: SettingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView(){
        binding.apply {
            btnCreateNotice.setOnClickListener {
                if(etTitle.text.isEmpty() || etContent.text.isEmpty()){
                    requireContext().showToast("항목을 모두 입력해 주세요")
                }else{
                    viewModel.insertQna(Qna(mainViewModel.user.value!!.seq, etTitle.text.toString(), etContent.text.toString(), mainViewModel.profile.value!!.nickname))
                    requireContext().showToast("등록 되었습니다")
                    findNavController().popBackStack()
                }
            }
            commonTopBack.setOnClickListener {
                findNavController().popBackStack()
            }

        }
    }

    private fun initViewModel(){

    }

}