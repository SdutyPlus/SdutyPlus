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
import com.d108.sduty.common.*
import com.d108.sduty.databinding.DialogTagSelectOneBinding
import com.d108.sduty.ui.sign.viewmodel.TagViewModel

private const val TAG = "TagSelectOneFragment"
class TagSelectOneFragment(var mContext: Context, var flag: Int) : DialogFragment() {
    private lateinit var binding: DialogTagSelectOneBinding
    private val viewModel: TagViewModel by viewModels()

    private lateinit var jobAdapter: TagAdapter
    private lateinit var interestAdapter: TagAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTagSelectOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(flag == FLAG_STUDY || flag == FLAG_STUDY_REGIST){
            binding.recyclerJob.visibility = View.GONE
        }
        initView()
        initViewModel()
    }

    fun initView(){
        jobAdapter = TagAdapter(JOB_TAG)
        jobAdapter.onClickTagItem = object: TagAdapter.OnClickTagListener{
            override fun onClick(view: View, position: Int, tagName: String) {
                onDismissDialogListener.onDismiss(tagName, JOB_TAG)
                dismiss()
            }
        }
        interestAdapter = TagAdapter(INTEREST_TAG)
        interestAdapter.onClickTagItem = object : TagAdapter.OnClickTagListener{
            override fun onClick(view: View, position: Int, tagName: String) {
                onDismissDialogListener.onDismiss(tagName, INTEREST_TAG)
                dismiss()
            }
        }

        binding.apply {
            if(flag == FLAG_STUDY_REGIST){
                tvSelectAll.visibility = View.GONE
            }
            recyclerJob.apply {
                adapter = jobAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
            }
            recyclerInterest.apply {
                adapter = interestAdapter
                layoutManager = GridLayoutManager(requireContext(), 3)
            }
            tvSelectAll.setOnClickListener {
                onDismissDialogListener.onDismiss("전체", ALL_TAG)
                dismiss()
            }
        }
    }

    fun initViewModel(){
        viewModel.apply {
            getJobListValue()
            getInterestListValue()
            jobList.observe(viewLifecycleOwner){
                jobAdapter.jobList = it
            }
            interestList.observe(viewLifecycleOwner){
                interestAdapter.interestList = it
            }
        }
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
        if(flag == FLAG_STUDY || flag == FLAG_STUDY_REGIST)
            params?.height = (deviceHeight * 0.60).toInt()
        else
            params?.height = (deviceHeight * 0.80).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    lateinit var onDismissDialogListener: OnDismissDialogListener
    interface OnDismissDialogListener{
        fun onDismiss(tagName: String, flag: Int)
    }
}
