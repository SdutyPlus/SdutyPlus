package com.d108.sduty.ui.main.study

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.d108.sduty.R
import com.d108.sduty.common.FLAG_STUDY_REGIST
import com.d108.sduty.common.SENDBIRD_APP_ID
import com.d108.sduty.databinding.FragmentStudyRegistBinding
import com.d108.sduty.model.dto.Alarm
import com.d108.sduty.model.dto.Study
import com.d108.sduty.ui.main.study.viewmodel.StudyRegisteViewModel
import com.d108.sduty.ui.sign.dialog.TagSelectOneFragment
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.Status
import com.d108.sduty.utils.showToast
import com.sendbird.calls.SendBirdCall
import com.sendbird.calls.SendBirdError

// 스터디 등록 - 스터디 명, 공개 설정, 비밀번호 설정, 최대 인원, 카테고리, 스터디 설명, 일반/캠스터디 설정
private const val TAG ="StudyRegistFragment"
class StudyRegistFragment : Fragment() {
    private lateinit var binding: FragmentStudyRegistBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private val studyRegisteViewModel: StudyRegisteViewModel by viewModels()
    private val args: StudyRegistFragmentArgs by navArgs()
    private var category: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel.displayBottomNav(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStudyRegistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        SendBirdCall.init(requireActivity().applicationContext, SENDBIRD_APP_ID)
        studyRegisteViewModel.authenticate(mainViewModel.user.value!!.name)

        when(args.type){
            false -> {
                binding.dailyTime.visibility = View.GONE
                binding.dailyWeek.visibility = View.GONE
                binding.tvDaily.visibility = View.GONE
            }
            else -> {}
        }

        val peopleData:Array<String> = resources.getStringArray(R.array.array_people)
        val peopleAdapter = ArrayAdapter(requireContext(), com.airbnb.lottie.R.layout.support_simple_spinner_dropdown_item, peopleData)
        binding.spinnerPeople.adapter = peopleAdapter

        binding.apply {
            btnCreateStudy.setOnClickListener { studyCreate() }
            etStudyName.addTextChangedListener(textChangeListener)
            commonTopBack.setOnClickListener {
                findNavController().popBackStack()
            }

            studyRegistJob.text = mainViewModel.profile.value!!.job


            studyRegistCategory.setOnClickListener {
                TagSelectOneFragment(requireContext(), FLAG_STUDY_REGIST).let{
                    it.show(parentFragmentManager, null)
                    it.onDismissDialogListener = object : TagSelectOneFragment.OnDismissDialogListener{
                        @SuppressLint("ResourceAsColor")
                        override fun onDismiss(tagName: String, flag: Int) {
                            studyRegistCategory.setTextColor(R.color.purple_200)
                            studyRegistCategory.setBackgroundResource(R.drawable.btn_study_regist)
                            studyRegistCategory.text = tagName
                            category = tagName
                        }
                    }
                }
            }



            switchPass.setOnCheckedChangeListener { buttonView, isChecked ->
                etStduyPass.isEnabled = isChecked
                if(isChecked){
                    etStduyPass.setBackgroundResource(0)
                } else{
                    etStduyPass.setBackgroundResource(R.drawable.study_gray)
                    etStduyPass.text = null
                }

            }

            btnMon.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    btnMon.setBackgroundResource(R.drawable.daily_click)
                    btnMon.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    btnMon.setBackgroundResource(R.drawable.border_study_solid)
                    btnMon.setTextColor(Color.parseColor("#979797"))
                }
            }

            btnTue.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    btnTue.setBackgroundResource(R.drawable.daily_click)
                    btnTue.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    btnTue.setBackgroundResource(R.drawable.border_study_solid)
                    btnTue.setTextColor(Color.parseColor("#979797"))
                }
            }

            btnWed.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    btnWed.setBackgroundResource(R.drawable.daily_click)
                    btnWed.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    btnWed.setBackgroundResource(R.drawable.border_study_solid)
                    btnWed.setTextColor(Color.parseColor("#979797"))
                }
            }

            btnThur.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    btnThur.setBackgroundResource(R.drawable.daily_click)
                    btnThur.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    btnThur.setBackgroundResource(R.drawable.border_study_solid)
                    btnThur.setTextColor(Color.parseColor("#979797"))
                }
            }

            btnFri.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    btnFri.setBackgroundResource(R.drawable.daily_click)
                    btnFri.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    btnFri.setBackgroundResource(R.drawable.border_study_solid)
                    btnFri.setTextColor(Color.parseColor("#979797"))
                }
            }

            btnSat.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    btnSat.setBackgroundResource(R.drawable.daily_click)
                    btnSat.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    btnSat.setBackgroundResource(R.drawable.border_study_solid)
                    btnSat.setTextColor(Color.parseColor("#979797"))
                }
            }

            btnSun.setOnCheckedChangeListener { buttonView, isChecked ->
                if(isChecked){
                    btnSun.setBackgroundResource(R.drawable.daily_click)
                    btnSun.setTextColor(Color.parseColor("#9585EB"))
                } else{
                    btnSun.setBackgroundResource(R.drawable.border_study_solid)
                    btnSun.setTextColor(Color.parseColor("#979797"))
                }
            }


            lifecycleOwner = this@StudyRegistFragment
            studyRegisteVM = studyRegisteViewModel

        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun studyCreate(){
        binding.apply {
            val name = etStudyName.text.toString().trim()
            var pass = etStduyPass.text?.toString()?.trim()
            val introduce = etStudyIntroduce.text.toString().trim()
            val people = spinnerPeople.selectedItem.toString()
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute

            if(name.isEmpty() || introduce.isEmpty()){
                context?.showToast("빈 칸을 모두 입력해 주세요.")
            } else if(category == ""){
                context?.showToast("공부 분야를 선택해주세요.")
            }
            else{
                studyRegisteViewModel.createSuccess.observe(viewLifecycleOwner){
                    if(it){
                        // 성공적으로 스터디 생성 - 스터디 이동? 내 스터디 리스트?
                        findNavController().popBackStack()
                    }
                }
                if(pass == ""){
                    pass = null
                }
                if(!args.type) {
                    studyRegisteViewModel.studyCreate(
                        Study(
                            mainViewModel.profile.value!!.userSeq, name,
                            introduce, category, people.toInt(), pass, null
                        )
                    )
                } else{
                    studyRegisteViewModel.createRoom()
                    studyRegisteViewModel.createRoomId.observe(requireActivity()){ resources ->

                        when (resources.status){
                            Status.LOADING -> {
                                // TODO : show loading view
                            }
                            Status.SUCCESS -> {
                                resources.data?.let {
                                        studyRegisteViewModel.camStudyCreate(
                                            Study(
                                                mainViewModel.profile.value!!.userSeq,
                                                name, introduce, category, people.toInt(), pass, it
                                            ), Alarm(0, "${hour}:${minute}:00", btnMon.isChecked, btnTue.isChecked,
                                                btnWed.isChecked, btnThur.isChecked, btnFri.isChecked, btnSat.isChecked, btnSun.isChecked)
                                        )
                                }
                            }
                            Status.ERROR -> {
                                val message = if (resources?.errorCode == SendBirdError.ERR_INVALID_PARAMS){
                                    getString(R.string.dashboard_invalid_room_params)
                                } else{
                                    resources?.message
                                }
                                AlertDialog.Builder(context)
                                    .setTitle(getString(R.string.dashboard_can_not_create_room))
                                    .setMessage(message ?: "unknown sendbird error")
                                    .setPositiveButton(R.string.ok, null)
                                    .create()
                                    .show()
                            }
                        }
                    }

                }



            }
        }
    }

    private val textChangeListener = object : TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            studyRegisteViewModel.getStudyId(binding.etStudyName.text.toString())
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        mainViewModel.displayBottomNav(true)
    }

}