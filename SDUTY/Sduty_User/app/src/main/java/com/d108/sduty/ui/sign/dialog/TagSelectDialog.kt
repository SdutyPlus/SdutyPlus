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
import androidx.recyclerview.widget.GridLayoutManager
import com.d108.sduty.adapter.TagAdapter
import com.d108.sduty.common.INTEREST_TAG
import com.d108.sduty.common.JOB_TAG
import com.d108.sduty.common.PROFILE
import com.d108.sduty.databinding.DialogTagSelectBinding
import com.d108.sduty.model.dto.InterestHashtag
import com.d108.sduty.model.dto.JobHashtag
import com.d108.sduty.ui.sign.viewmodel.TagViewModel
import com.d108.sduty.utils.showToast

private const val TAG ="TagSelectDialog"
class TagSelectDialog(val mContext: Context) : DialogFragment() {
    private lateinit var binding: DialogTagSelectBinding
    private lateinit var interestAdapter: TagAdapter
    private lateinit var jobAdapter: TagAdapter
    private lateinit var selectedInterestAdapter: TagAdapter
    private lateinit var selectedJobAdapter: TagAdapter

    private var jobList = mutableListOf<JobHashtag>()
    private var interestList = mutableListOf<InterestHashtag>()
    private var selectedJobList = mutableListOf<JobHashtag>()
    private var selectedInterestList = mutableListOf<InterestHashtag>()

    lateinit var onClickConfirm: OnClickConfirm

    private val tagViewModel: TagViewModel by viewModels()
    private var flag = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTagSelectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        flag = requireArguments().getInt("flag")
        if(flag != PROFILE){
            tagViewModel.setJobVisible()
            binding.recyclerSelectedJob.visibility = View.GONE
        }
        initViewModel()

    }

    private fun initViewModel(){
        tagViewModel.apply {
            getInterestListValue()
            getJobListValue()
            initView()
            interestList.observe(viewLifecycleOwner){
                this@TagSelectDialog.interestList = it
                interestAdapter.interestList = it

            }
            jobList.observe(viewLifecycleOwner){
                this@TagSelectDialog.jobList = it
                jobAdapter.jobList = it

            }
        }
    }

    private fun initView(){
        binding.apply {
            vm = tagViewModel
            lifecycleOwner = this@TagSelectDialog
        }

        interestAdapter = TagAdapter(INTEREST_TAG)
        interestAdapter.onClickTagItem = object : TagAdapter.OnClickTagListener{
            override fun onClick(view: View, position: Int, tagName: String) {
                selectedInterestList.add(interestList[position])
                interestList.removeAt(position)
                interestAdapter.interestList = interestList
                selectedInterestAdapter.interestList = selectedInterestList
                tagViewModel.setInterestVisible(selectedInterestList.size)
            }
        }

        jobAdapter = TagAdapter(JOB_TAG)
        jobAdapter.onClickTagItem = object : TagAdapter.OnClickTagListener{
            override fun onClick(view: View, position: Int, tagName: String) {
                selectedJobList.add(jobList[position])
                jobList.removeAt(position)
                jobAdapter.jobList = jobList
                selectedJobAdapter.jobList = selectedJobList
                tagViewModel.setJobVisible()
            }
        }

        selectedInterestAdapter = TagAdapter(INTEREST_TAG)
        selectedInterestAdapter.onClickTagItem = object : TagAdapter.OnClickTagListener{
            override fun onClick(view: View, position: Int, tagName: String) {
                interestList.add(selectedInterestList[position])
                selectedInterestList.removeAt(position)
                interestAdapter.interestList = interestList
                selectedInterestAdapter.interestList = selectedInterestList
                tagViewModel.setInterestVisible(selectedInterestList.size)
            }
        }

        selectedJobAdapter = TagAdapter(JOB_TAG)
        selectedJobAdapter.onClickTagItem = object : TagAdapter.OnClickTagListener{
            override fun onClick(view: View, position: Int, tagName: String) {
                jobList.add(selectedJobList[position])
                selectedJobList.removeAt(position)
                jobAdapter.jobList = jobList
                selectedJobAdapter.jobList = selectedJobList
                tagViewModel.setJobVisible()
            }
        }
        binding.apply {
            recyclerInterest.apply {
                adapter = interestAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
            }
            recyclerJob.apply {
                adapter = jobAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
            }
            recyclerSelectedInterest.apply {
                adapter = selectedInterestAdapter
                if(flag != PROFILE) {
                    layoutManager = GridLayoutManager(requireContext(), 3)
                }
                else{
                    layoutManager = GridLayoutManager(requireContext(), 2)
                }
            }
            recyclerSelectedJob.apply {
                adapter = selectedJobAdapter
                layoutManager = GridLayoutManager(requireContext(), 1)
            }
            btnConfirm.setOnClickListener {
                if(flag == PROFILE && selectedJobList.size == 0){
                    requireContext().showToast("직업을 선택해 주세요")
                    return@setOnClickListener
                }
                var selectedJob: JobHashtag? = null
                if(selectedJobList.size > 0){
                    selectedJob = selectedJobList[0]
                }
                onClickConfirm.onClick(selectedJob, selectedInterestList)
                dismiss()
            }
        }
    }

    interface OnClickConfirm{
        fun onClick(selectedJobList: JobHashtag?, selectedInterestList: MutableList<InterestHashtag>)
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