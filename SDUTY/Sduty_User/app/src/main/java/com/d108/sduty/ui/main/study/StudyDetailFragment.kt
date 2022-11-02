package com.d108.sduty.ui.main.study

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.d108.sduty.R
import com.d108.sduty.adapter.StudyMemberAdapter
import com.d108.sduty.databinding.FragmentStudyDetailBinding
import com.d108.sduty.model.dto.Member
import com.d108.sduty.ui.MainActivity
import com.d108.sduty.ui.main.study.dialog.StudyCheckDialog
import com.d108.sduty.ui.main.study.dialog.StudyDialog
import com.d108.sduty.ui.main.study.viewmodel.StudyDetailViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.safeNavigate
import com.d108.sduty.utils.showToast
import kotlin.math.roundToInt

// 스터디 상세 -
private const val TAG = "StudyDetailFragment"
class StudyDetailFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var mainActivity: MainActivity
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentStudyDetailBinding
    private val studyDetailViewModel: StudyDetailViewModel by viewModels()
    private val args: StudyDetailFragmentArgs by navArgs()
    lateinit var masterNickname: String
    lateinit var masterJob: String

    private lateinit var studyMemberAdapter: StudyMemberAdapter
    private lateinit var studyMember: List<Map<String, Any>>

    private lateinit var memberList: ArrayList<Member>




    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel.displayBottomNav(false)
        mainActivity = context as MainActivity
    }

    override fun onRefresh() {
        studyDetailViewModel.getMyStudyInfo(mainViewModel.profile.value!!.userSeq, args.studySeq)
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        
        studyDetailViewModel.myStudyInfo.observe(viewLifecycleOwner){ map ->
            if(map != null){
                val studyInfo = studyDetailViewModel.myStudyInfo.value as Map<String, Any>

                studyMember = studyInfo["members"] as List<Map<String, Any>>
                //memberList = studyMember as ArrayList<Member>
                memberList = ArrayList<Member>()
                for(member in studyMember){
                    memberList.add(Member(
                        member["is_studying"].toString().toDouble().roundToInt(),
                        member["nickname"] as String,
                        member["total_time"] as String,
                        member["userSeq"].toString().toDouble().roundToInt()))
                }

                initAdapter()

                var joinNum = ((studyInfo["study"] as Map<String, Any>)["joinNumber"].toString()).toDouble()
                var limitNum = ((studyInfo["study"] as Map<String, Any>)["limitNumber"].toString()).toDouble()

                if((studyInfo["study"] as Map<String, Any>)["password"] == null){
                    binding.imgStudyLock.setImageResource(R.drawable.img_study_detail_unlock)
                } else{
                    binding.imgStudyLock.setImageResource(R.drawable.img_study_detail_lock)
                }

                if((studyInfo["study"] as Map<String, Any>)["masterSeq"].toString().toDouble().roundToInt() == mainViewModel.profile.value!!.userSeq){
                    binding.btnStudySetting.visibility = View.VISIBLE
                    binding.btnStudyExit.visibility = View.GONE
                } else{
                    binding.btnStudySetting.visibility = View.GONE
                    binding.btnStudyExit.visibility = View.VISIBLE
                }

                binding.studyDetailCategory.text = "#일반스터디" + "#" + (studyInfo["study"] as Map<String, Any>)["category"].toString()

                binding.studyDetailName.text = (studyInfo["study"] as Map<String, Any>)["name"].toString()
                binding.studyDetailJoinnum.text = joinNum.roundToInt().toString()
                binding.studyDetailLimitnum.text = limitNum.roundToInt().toString()
                binding.studyDetailIntroduce.text = (studyInfo["study"] as Map<String, Any>)["introduce"].toString()
                binding.studyDetailNotice.text = (studyInfo["study"] as Map<String, Any>)["notice"].toString()

                binding.studyDetailIntroduce.setOnClickListener {
                    val dialog = StudyCheckDialog(mainActivity, "그룹 소개", (studyInfo["study"] as Map<String, Any>)["introduce"].toString() )
                    dialog.showDialog()
                }
                binding.studyDetailNotice.setOnClickListener {
                    val dialog = StudyCheckDialog(mainActivity, "공지사항", (studyInfo["study"] as Map<String, Any>)["notice"].toString() )
                    dialog.showDialog()
                }

                binding.swipeRefresh.setOnRefreshListener(this)


                studyDetailViewModel.masterNickname((studyInfo["study"] as Map<String, Any>)["masterSeq"].toString().toDouble().roundToInt())
                studyDetailViewModel.studyMasterNickName.observe(viewLifecycleOwner) {
                    masterNickname = it.nickname
                    masterJob = it.job
                    binding.studyDetailMaster.text = masterNickname
                    binding.studyDetailCategory.text = "#일반스터디" + "#" + masterJob + "#" + (studyInfo["study"] as Map<String, Any>)["category"].toString()
                }
            }
        }

        studyDetailViewModel.isQuitStudy.observe(viewLifecycleOwner){
            if(it){
                context?.showToast("스터디 탈퇴가 완료되었습니다.")
                findNavController().popBackStack()
            }
        }

        studyDetailViewModel.getMyStudyInfo(mainViewModel.profile.value!!.userSeq, args.studySeq)

        binding.apply {
            btnStudySetting.setOnClickListener {
                findNavController().safeNavigate(StudyDetailFragmentDirections.actionStudyDetailFragmentToStudySettingFragment(args.studySeq, masterNickname))
            }

            btnStudyExit.setOnClickListener {
                val dialog = StudyDialog(mainActivity, "탈퇴하시겠습니까?", "가입하신 스터디에서 탈퇴합니다.", "탈퇴", "취소")
                dialog.showDialog()
                dialog.setOnClickListener(object : StudyDialog.OnDialogClickListener{
                    override fun onClicked() {
                        studyDetailViewModel.studyQuit(args.studySeq, mainViewModel.profile.value!!.userSeq)
                    }
                })
            }

            commonTopBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun initAdapter(){
        studyMemberAdapter = memberList?.let { StudyMemberAdapter(it) }!!
        studyMemberAdapter.onClickMemberListener = object : StudyMemberAdapter.OnClickMemberListener {
            override fun onClick(member: Member) {
                if(member.userSeq == mainViewModel.user.value!!.seq) {
                    findNavController().safeNavigate(StudyDetailFragmentDirections.actionStudyDetailFragmentToMyPageFragment())
                }else{
                    findNavController().safeNavigate(StudyDetailFragmentDirections.actionStudyDetailFragmentToUserProfileFragment(member.userSeq!!))
                }
            }

        }
        binding.studyMember.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = studyMemberAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.displayBottomNav(true)
    }


}