package com.d205.sdutyplus.view.report.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.d205.domain.model.report.Task
import com.d205.sdutyplus.databinding.DialogConfirmBinding
import com.d205.sdutyplus.view.report.ReportViewModel
import com.d205.sdutyplus.view.timer.viewmodel.TimerViewModel
import kotlin.concurrent.timer

class ConfirmDialog(private val task_seq: Long): DialogFragment() {
    private lateinit var binding: DialogConfirmBinding
    private val timerViewModel: TimerViewModel by activityViewModels()
    private val reportViewModel: ReportViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogConfirmBinding.inflate(inflater, container, false)
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnRemove.setOnClickListener {
                if( requireArguments().getString("Action") == "DeleteTask"){
                    timerViewModel.timerTimeReset()
                } else{
                    // Task 삭제 함수
                    reportViewModel.deleteTask(task_seq)
                }



                dismiss()
            }
            btnCancel.setOnClickListener { 
                dismiss()
            }
        }
    }
}