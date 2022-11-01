package com.d205.sdutyplus.view.join

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.d205.domain.model.common.JobHashtag
import com.d205.sdutyplus.databinding.DialogTagSelectBinding
import com.d205.sdutyplus.uitls.PROFILE
import com.d205.sdutyplus.uitls.showToast

private const val TAG ="TagSelectDialog"
class TagSelectDialog(val mContext: Context) : DialogFragment() {
    private lateinit var binding: DialogTagSelectBinding
    private lateinit var jobAdapter: TagAdapter
    private lateinit var selectedJobAdapter: TagAdapter

    private var jobList = mutableListOf<JobHashtag>()
    private var selectedJobList = mutableListOf<JobHashtag>()

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
        initViewModel()
    }

    private fun initViewModel(){
        tagViewModel.apply {
            getJobListValue()
            initView()

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


        jobAdapter = TagAdapter()
        jobAdapter.onClickTagItem = object : TagAdapter.OnClickTagListener{
            override fun onClick(view: View, position: Int, tagName: String) {
//                selectedJobList.add(jobList[position])
//                jobList.removeAt(position)
//                jobAdapter.jobList = jobList
//                selectedJobAdapter.jobList = selectedJobList
//                tagViewModel.setJobVisible()

                selectedJobList.clear()
                selectedJobList.add(jobList[position])
                Log.d(TAG, "onClick: $selectedJobList")
                selectedJobAdapter.jobList = selectedJobList
            }
        }


        selectedJobAdapter = TagAdapter()
        selectedJobAdapter.onClickTagItem = object : TagAdapter.OnClickTagListener{
            override fun onClick(view: View, position: Int, tagName: String) {
//                jobList.add(selectedJobList[position])
//                selectedJobList.removeAt(position)
//                jobAdapter.jobList = jobList
//                selectedJobAdapter.jobList = selectedJobList
//                tagViewModel.setJobVisible()

                selectedJobList.clear()
                selectedJobAdapter.jobList = selectedJobList
            }
        }
        binding.apply {
            recyclerJob.apply {
                adapter = jobAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
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
                onClickConfirm.onClick(selectedJob)
                dismiss()
            }
        }
    }

    interface OnClickConfirm{
        fun onClick(selectedJobList: JobHashtag?)
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