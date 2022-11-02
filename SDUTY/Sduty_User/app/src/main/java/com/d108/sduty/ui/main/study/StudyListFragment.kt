package com.d108.sduty.ui.main.study

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.d108.sduty.R
import com.d108.sduty.adapter.StudyListAdapter
import com.d108.sduty.common.FLAG_STUDY
import com.d108.sduty.databinding.FragmentStudyListBinding
import com.d108.sduty.model.dto.Study
import com.d108.sduty.ui.MainActivity
import com.d108.sduty.ui.main.study.dialog.StudyDetailDialog
import com.d108.sduty.ui.main.study.dialog.StudyPasswordDialog
import com.d108.sduty.ui.main.study.viewmodel.StudyListViewModel
import com.d108.sduty.ui.sign.dialog.TagSelectOneFragment
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.safeNavigate
import com.d108.sduty.utils.showToast

// 스터디 전체 리스트
private const val TAG = "StudyListFragment"
class StudyListFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener{
    private lateinit var mainActivity: MainActivity
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentStudyListBinding

    private val studyListViewModel: StudyListViewModel by viewModels()
    private lateinit var studyListAdapter: StudyListAdapter
    private lateinit var studyList: List<Study>
    private var category: String = "전체"


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel.displayBottomNav(false)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onRefresh() {
        studyListViewModel.getStudyList()
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        studyListViewModel.isJoinStudySuccess.observe(viewLifecycleOwner){
            if(it){
                context?.showToast("가입이 완료되었습니다.")
                findNavController().popBackStack()
            }else{
                context?.showToast("이미 가입된 그룹입니다.")
            }
        }

        studyListViewModel.studyList.observe(viewLifecycleOwner){
            if(it != null){
                studyList = studyListViewModel.studyList.value as List<Study>
                initAdapter()
            }
        }
        studyListViewModel.getStudyList()

        binding.apply {
            swipeRefresh.setOnRefreshListener(this@StudyListFragment)
            tbCategory.setOnClickListener {
                TagSelectOneFragment(requireContext(), FLAG_STUDY).let{
                    it.show(parentFragmentManager, null)
                    it.onDismissDialogListener = object : TagSelectOneFragment.OnDismissDialogListener{
                        override fun onDismiss(tagName: String, flag: Int) {
                            binding.tbCategory.text = tagName
                            category = tagName
                            studyListViewModel.getStudyFilter(tagName, tbPeople.isChecked, tbCamstudy.isChecked, tbPublic.isChecked)
                            tbCategory.setBackgroundResource(R.drawable.btn_study_regist)
                            tbCategory.setTextColor(Color.BLACK)
                        }
                    }
                }
            }

            tbCamstudy.setOnCheckedChangeListener { buttonView, isChecked ->
                studyListViewModel.getStudyFilter(category, tbPeople.isChecked, isChecked, tbPublic.isChecked)
                if(isChecked){
                    tbCamstudy.setBackgroundResource(R.drawable.btn_study_regist)
                    tbCamstudy.setTextColor(Color.BLACK)
                } else{
                    tbCamstudy.setBackgroundResource(R.drawable.btn_study_filter)
                    tbCamstudy.setTextColor(Color.parseColor("#616161"))
                }
            }

            tbPeople.setOnCheckedChangeListener { buttonView, isChecked ->
                studyListViewModel.getStudyFilter(category, isChecked, tbCamstudy.isChecked, tbPublic.isChecked)
                if(isChecked){
                    tbPeople.setBackgroundResource(R.drawable.btn_study_regist)
                    tbPeople.setTextColor(Color.BLACK)
                } else{
                    tbPeople.setBackgroundResource(R.drawable.btn_study_filter)
                    tbPeople.setTextColor(Color.parseColor("#616161"))
                }
            }

            tbPublic.setOnCheckedChangeListener { buttonView, isChecked ->
                studyListViewModel.getStudyFilter(category, tbPeople.isChecked, tbCamstudy.isChecked, isChecked)
                if(isChecked){
                    tbPublic.setBackgroundResource(R.drawable.btn_study_regist)
                    tbPublic.setTextColor(Color.BLACK)
                } else{
                    tbPublic.setBackgroundResource(R.drawable.btn_study_filter)
                    tbPublic.setTextColor(Color.parseColor("#616161"))
                }
            }

            btnSearch.setOnClickListener {
                findNavController().safeNavigate(StudyListFragmentDirections.actionStudyListFragmentToStudySearchFragment())
            }
            commonTopBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.displayBottomNav(true)
    }

    private fun initAdapter(){
        studyListAdapter = StudyListAdapter(studyList)
        studyListAdapter.onStudyItemClick = object : StudyListAdapter.OnStudyItemClick{
            override fun onClick(view: View, position: Int) {
                var num = 0
                studyListViewModel.studyDetail(studyList[position].seq)
                studyListViewModel.studyDetail.observe(viewLifecycleOwner){
                    if(num == 0){
                        // 전체 스터디 조회 - 스터디 클릭 시 상세정보 다이얼로그
                        val dialog = StudyDetailDialog(mainActivity, studyList[position], it.masterNickname, it.masterJob)
                        dialog.showDialog()
                        dialog.setOnClickListener(object : StudyDetailDialog.OnDialogClickListener{
                            override fun onClicked() {
                                if(studyList[position].password != null){
                                    val dialogPass = StudyPasswordDialog(mainActivity)
                                    dialogPass.showDialog()
                                    dialogPass.setOnClickListener(object : StudyPasswordDialog.OnDialogClickListener{
                                        override fun onClicked(etPassword: EditText) {
                                            if(studyList[position].joinNumber == studyList[position].limitNumber){
                                                context?.showToast("스터디 인원 수가 많아 참가할 수 없습니다.")
                                            } else{
                                                if(etPassword.text.toString() == studyList[position].password){
                                                    studyListViewModel.studyJoin(studyList[position].seq, mainViewModel.profile.value!!.userSeq)
                                                } else{
                                                    context?.showToast("비밀번호가 틀렸습니다.")
                                                }
                                            }
                                        }
                                    })
                                } else{
                                    if(studyList[position].joinNumber == studyList[position].limitNumber){
                                        context?.showToast("스터디 인원 수가 많아 참가할 수 없습니다.")
                                    } else{
                                        studyListViewModel.studyJoin(studyList[position].seq, mainViewModel.profile.value!!.userSeq)
                                    }
                                }

                            }
                        })
                        num += 1
                    }

                }

            }
        }
        binding.studyList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = studyListAdapter
        }
    }


}