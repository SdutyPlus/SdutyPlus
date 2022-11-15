package com.d108.sduty.ui.main.study

import android.Manifest
import android.content.Context
import android.graphics.Color
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
import com.d108.sduty.adapter.CamStudyMemberAdapter
import com.d108.sduty.databinding.FragmentCamstudyDetailBinding
import com.d108.sduty.model.dto.Member
import com.d108.sduty.ui.MainActivity
import com.d108.sduty.ui.main.study.dialog.StudyCheckDialog
import com.d108.sduty.ui.main.study.dialog.StudyDialog
import com.d108.sduty.ui.main.study.viewmodel.CamStudyDetailViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.safeNavigate
import com.d108.sduty.utils.showToast
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import kotlin.math.roundToInt


// 캠 스터디 상세
private const val TAG = "CamStudyDetailFragment"
class CamStudyDetailFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var mainActivity: MainActivity
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding: FragmentCamstudyDetailBinding
    private val camStudyDetailViewModel: CamStudyDetailViewModel by viewModels()
    private val args: CamStudyDetailFragmentArgs by navArgs()
    lateinit var masterNickname: String
    lateinit var masterJob: String
    lateinit var studyRoomId: String
    lateinit var studyName: String

    private lateinit var camStudyMemberAdapter: CamStudyMemberAdapter
    private lateinit var studyMember: List<Map<String, Any>>
    private lateinit var memberList: ArrayList<Member>


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel.displayBottomNav(false)
        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCamstudyDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onRefresh() {
        camStudyDetailViewModel.getCamStudyInfo(mainViewModel.profile.value!!.userSeq, args.studySeq)
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        camStudyDetailViewModel.camStudyInfo.observe(viewLifecycleOwner){map ->
            if(map != null){
                val studyInfo = camStudyDetailViewModel.camStudyInfo.value as Map<String, Any>

                studyMember = studyInfo["members"] as List<Map<String, Any>>

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
                binding.swipeRefresh.setOnRefreshListener(this)

                binding.studyDetailCategory.text = "#캠스터디" + "#" + (studyInfo["study"] as Map<String, Any>)["category"].toString()

                binding.studyDetailName.text = (studyInfo["study"] as Map<String, Any>)["name"].toString()
                binding.studyDetailJoinnum.text = joinNum.roundToInt().toString()
                binding.studyDetailLimitnum.text = limitNum.roundToInt().toString()
                binding.studyDetailIntroduce.text = (studyInfo["study"] as Map<String, Any>)["introduce"].toString()
                binding.studyDetailNotice.text = (studyInfo["study"] as Map<String, Any>)["notice"].toString()

                studyRoomId = (studyInfo["study"] as Map<String, Any>)["roomId"].toString()
                studyName = (studyInfo["study"] as Map<String, Any>)["name"].toString()

                val alarm = ((studyInfo["study"] as Map<String, Any>)["alarm"] as Map<String, Any>)
                if(alarm["mon"] == true){
                    binding.tvMon.setBackgroundResource(R.drawable.daily_click)
                    binding.tvMon.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    binding.tvMon.setBackgroundResource(R.drawable.border_study_solid)
                    binding.tvMon.setTextColor(Color.parseColor("#979797"))
                }

                if(alarm["tue"] == true){
                    binding.tvTue.setBackgroundResource(R.drawable.daily_click)
                    binding.tvTue.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    binding.tvTue.setBackgroundResource(R.drawable.border_study_solid)
                    binding.tvTue.setTextColor(Color.parseColor("#979797"))
                }

                if(alarm["wed"] == true){
                    binding.tvWed.setBackgroundResource(R.drawable.daily_click)
                    binding.tvWed.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    binding.tvWed.setBackgroundResource(R.drawable.border_study_solid)
                    binding.tvWed.setTextColor(Color.parseColor("#979797"))
                }

                if(alarm["thu"] == true){
                    binding.tvThu.setBackgroundResource(R.drawable.daily_click)
                    binding.tvThu.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    binding.tvThu.setBackgroundResource(R.drawable.border_study_solid)
                    binding.tvThu.setTextColor(Color.parseColor("#979797"))
                }

                if(alarm["fri"] == true){
                    binding.tvFri.setBackgroundResource(R.drawable.daily_click)
                    binding.tvFri.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    binding.tvFri.setBackgroundResource(R.drawable.border_study_solid)
                    binding.tvFri.setTextColor(Color.parseColor("#979797"))
                }

                if(alarm["sat"] == true){
                    binding.tvSat.setBackgroundResource(R.drawable.daily_click)
                    binding.tvSat.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    binding.tvSat.setBackgroundResource(R.drawable.border_study_solid)
                    binding.tvSat.setTextColor(Color.parseColor("#979797"))
                }

                if(alarm["sun"] == true){
                    binding.tvSun.setBackgroundResource(R.drawable.daily_click)
                    binding.tvSun.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    binding.tvSun.setBackgroundResource(R.drawable.border_study_solid)
                    binding.tvSun.setTextColor(Color.parseColor("#979797"))
                }

                binding.tvTime.text = "매주 " + alarm["time"].toString().substring(0 until 5)

                binding.studyDetailIntroduce.setOnClickListener {
                    val dialog = StudyCheckDialog(mainActivity, "그룹 소개", (studyInfo["study"] as Map<String, Any>)["introduce"].toString() )
                    dialog.showDialog()
                }
                binding.studyDetailNotice.setOnClickListener {
                    val dialog = StudyCheckDialog(mainActivity, "공지사항", (studyInfo["study"] as Map<String, Any>)["notice"].toString() )
                    dialog.showDialog()
                }

                camStudyDetailViewModel.masterNickname((studyInfo["study"] as Map<String, Any>)["masterSeq"].toString().toDouble().roundToInt())
                camStudyDetailViewModel.studyMasterNickName.observe(viewLifecycleOwner) {
                    masterNickname = it.nickname
                    masterJob = it.job
                    binding.studyDetailMaster.text = masterNickname
                    binding.studyDetailCategory.text = "#일반스터디" + "#" + masterJob + "#" + (studyInfo["study"] as Map<String, Any>)["category"].toString()
                }
            }
        }

        camStudyDetailViewModel.isQuitStudy.observe(viewLifecycleOwner){
            if(it){
                context?.showToast("스터디 탈퇴가 완료되었습니다.")
                findNavController().popBackStack()
            }
        }


        camStudyDetailViewModel.getCamStudyInfo(mainViewModel.profile.value!!.userSeq, args.studySeq)

        binding.apply {

            btnJoinCamstudy.setOnClickListener {
                checkPermission()
            }

            btnStudySetting.setOnClickListener {
                findNavController().safeNavigate(CamStudyDetailFragmentDirections.actionCamStudyDetailFragmentToStudySettingFragment(args.studySeq, masterNickname))
            }

            btnStudyExit.setOnClickListener {
                val dialog = StudyDialog(mainActivity, "탈퇴하시겠습니까?", "가입하신 스터디에서 탈퇴합니다.", "탈퇴", "취소")
                dialog.showDialog()
                dialog.setOnClickListener(object : StudyDialog.OnDialogClickListener {
                    override fun onClicked() {
                        camStudyDetailViewModel.studyQuit(
                            args.studySeq,
                            mainViewModel.profile.value!!.userSeq
                        )
                    }
                })
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
        camStudyMemberAdapter = memberList?.let { CamStudyMemberAdapter(it) }!!
        camStudyMemberAdapter.onClickMemberListener = object : CamStudyMemberAdapter.OnClickMemberListener{
            override fun onClick(member: Member) {
                if(member.userSeq == mainViewModel.user.value!!.seq) {
                    findNavController().safeNavigate(CamStudyDetailFragmentDirections.actionCamStudyDetailFragmentToMyPageFragment())
                }else{
                    findNavController().safeNavigate(CamStudyDetailFragmentDirections.actionCamStudyDetailFragmentToUserProfileFragment(member.userSeq!!))
                }
            }
        }
        binding.studyMember.apply {
            layoutManager = GridLayoutManager(context, 4)
            adapter = camStudyMemberAdapter
        }
    }

    private fun checkPermission(){
        val permissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                findNavController().safeNavigate(CamStudyDetailFragmentDirections.actionCamStudyDetailFragmentToPreviewFragment(studyRoomId, studyName))
            }
            override fun onPermissionDenied(deniedPermissions: List<String>) {
                requireActivity().showToast("카메라 권한을 허용해야 이용이 가능합니다.")
            }

        }
        TedPermission.create()
            .setPermissionListener(permissionListener)
            .setDeniedMessage("권한을 허용해주세요. [설정] > [앱 및 알림] > [고급] > [앱 권한]")
            .setPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
            .check()
    }
}