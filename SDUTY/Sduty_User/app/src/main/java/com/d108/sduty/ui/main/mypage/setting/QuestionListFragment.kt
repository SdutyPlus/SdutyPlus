package com.d108.sduty.ui.main.mypage.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.d108.sduty.R
import com.d108.sduty.adapter.QnaAdapter
import com.d108.sduty.databinding.FragmentQuestionListBinding
import com.d108.sduty.model.dto.Qna
import com.d108.sduty.ui.main.mypage.setting.viewmodel.SettingViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.safeNavigate

private const val TAG ="QuestionListFragment"
class QuestionListFragment : Fragment() {
    private lateinit var binding: FragmentQuestionListBinding
    private val viewModel: SettingViewModel by viewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var qnaAdapter: QnaAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    private fun initView() {
        qnaAdapter = QnaAdapter()
        qnaAdapter.onClickQnaListener = object :QnaAdapter.OnClickQnaListener{
            override fun onClick(qna: Qna) {
                QuestionDetailDialog(qna).let {
                    it.show(parentFragmentManager, null)
                }
            }
        }
        binding.apply {
            recyclerQna.apply {
                adapter = qnaAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
            btnCreateNotice.setOnClickListener {
                findNavController().navigate(QuestionListFragmentDirections.actionQuestionListFragmentToQuestionFragment())
            }
            commonTopBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initViewModel(){
        viewModel.apply {
            getQnaList(mainViewModel.user.value!!.seq)
            qnaList.observe(viewLifecycleOwner){
                qnaAdapter.list = it
            }
        }
    }

}