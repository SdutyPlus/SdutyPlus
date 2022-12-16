package com.d205.sdutyplus.view.join

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
import androidx.recyclerview.widget.GridLayoutManager
import com.d205.domain.model.common.JobHashtag
import com.d205.sdutyplus.databinding.DialogTagSelectBinding
import com.d205.sdutyplus.utills.PROFILE
import com.d205.sdutyplus.utills.jobs
import com.d205.sdutyplus.utills.showToast

private const val TAG ="TagSelectDialog"
class TagSelectDialog(val mContext: Context, var curJobName: String) : DialogFragment() {
    private lateinit var binding: DialogTagSelectBinding
    private lateinit var jobAdapter: TagAdapter

    var selectedJob: JobHashtag? = null


    lateinit var onClickConfirm: OnClickConfirm
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
        initView()
    }


    private fun initView(){
        jobAdapter = TagAdapter()

        val jobList = mutableListOf<JobHashtag>()
        for(index in jobs.indices) {
            if(jobs[index].name == curJobName) {
                jobList.add(JobHashtag(jobs[index].seq, jobs[index].name, true))
                jobAdapter.setCurPosition(index)
                selectedJob = jobs[index]
            }
            else {
                jobList.add(jobs[index])
            }
        }

        jobAdapter.jobList = jobList
        jobAdapter.onClickTagItem = object : TagAdapter.OnClickTagListener{
            override fun onClick(jobHashtag: JobHashtag) {
                selectedJob = jobHashtag
            }
        }


        binding.apply {
            recyclerJob.apply {
                adapter = jobAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
            }

            btnConfirm.setOnClickListener {
                if(flag == PROFILE && selectedJob == null){
                    requireContext().showToast("직업을 선택해 주세요")
                    return@setOnClickListener
                }
                onClickConfirm.onClick(selectedJob)
                dismiss()
            }
        }
    }

    interface OnClickConfirm{
        fun onClick(selectedJob: JobHashtag?)
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
        params?.height = (deviceHeight * 0.45).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}