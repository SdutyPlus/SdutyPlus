package com.d108.sduty.ui.main.study

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.d108.sduty.adapter.StudyListAdapter
import com.d108.sduty.databinding.FragmentStudySearchBinding
import com.d108.sduty.model.dto.Study
import com.d108.sduty.ui.MainActivity
import com.d108.sduty.ui.main.study.dialog.StudyDetailDialog
import com.d108.sduty.ui.main.study.dialog.StudyPasswordDialog
import com.d108.sduty.ui.main.study.viewmodel.StudySearchViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.showToast

// 스터디 검색 - 스터디 명, 카테고리 별 검색
private const val TAG = "StudySearchFragment"
class StudySearchFragment : Fragment() {
    private lateinit var mainActivity: MainActivity
    private lateinit var binding: FragmentStudySearchBinding
    private val mainViewModel: MainViewModel by activityViewModels()

    private val studySearchViewModel: StudySearchViewModel by viewModels()
    private lateinit var studySearchAdapter: StudyListAdapter
    private lateinit var studyList: List<Study>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudySearchBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        studySearchViewModel.studySearchList.observe(viewLifecycleOwner){
            if(it != null){
                studyList = studySearchViewModel.studySearchList.value as List<Study>
                initAdapter()
            }
        }

        studySearchViewModel.isJoinStudySuccess.observe(viewLifecycleOwner){
            if(it){
                context?.showToast("가입이 완료되었습니다.")
                findNavController().popBackStack()
            }else{
                context?.showToast("이미 가입된 그룹입니다.")
            }
        }

        binding.studySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    studySearchViewModel.getStudySearchList(newText)
                }
                return true
            }

        })


        binding.commonTopBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter(){
        studySearchAdapter = StudyListAdapter(studyList)
        studySearchAdapter.onStudyItemClick = object : StudyListAdapter.OnStudyItemClick{
            override fun onClick(view: View, position: Int) {
                var num = 0
                studySearchViewModel.studyDetail(studyList[position].seq)
                studySearchViewModel.studyDetail.observe(viewLifecycleOwner) {
                    if (num == 0) {
                        // 전체 스터디 조회 - 스터디 클릭 시 상세정보 다이얼로그
                        val dialog = StudyDetailDialog(
                            mainActivity,
                            studyList[position],
                            it.masterNickname,
                            it.masterJob
                        )
                        dialog.showDialog()
                        dialog.setOnClickListener(object : StudyDetailDialog.OnDialogClickListener {
                            override fun onClicked() {
                                if (studyList[position].password != null) {
                                    val dialogPass = StudyPasswordDialog(mainActivity)
                                    dialogPass.showDialog()
                                    dialogPass.setOnClickListener(object :
                                        StudyPasswordDialog.OnDialogClickListener {
                                        override fun onClicked(etPassword: EditText) {
                                            if(studyList[position].joinNumber < studyList[position].limitNumber){
                                                if (etPassword.text.toString() == studyList[position].password) {
                                                    studySearchViewModel.studyJoin(
                                                        studyList[position].seq,
                                                        mainViewModel.profile.value!!.userSeq
                                                    )
                                                } else {
                                                    context?.showToast("비밀번호가 틀렸습니다.")
                                                }
                                            } else{
                                                context?.showToast("스터디 인원 수가 많아 참가할 수 없습니다.")
                                            }
                                        }
                                    })
                                } else {
                                    studySearchViewModel.studyJoin(
                                        studyList[position].seq,
                                        mainViewModel.profile.value!!.userSeq
                                    )
                                }

                            }
                        })
                        num += 1
                    }
                }
            }
        }
        binding.studySearchList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = studySearchAdapter
        }
    }

}