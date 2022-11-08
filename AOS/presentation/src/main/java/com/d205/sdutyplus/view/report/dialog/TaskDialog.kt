package com.d205.sdutyplus.view.report.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.d205.domain.model.report.Task
import com.d205.domain.utils.ResultState
import com.d205.sdutyplus.databinding.DialogTaskBinding
import com.d205.sdutyplus.view.report.ReportViewModel
import com.d205.sdutyplus.view.timer.viewmodel.TimerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TaskDialog(private val task: Task): DialogFragment() {
    private lateinit var binding: DialogTaskBinding
    private val reportViewModel: ReportViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogTaskBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val action = arguments?.getString("Action", "Add")

        when(action){
            "CustomAdd" ->{

            }
            "Info" -> {
                InfoTask()
            }
        }
    }



    private fun InfoTask() {
        binding.apply {
            tvStartTime.text = task.startTime
            tvEndTime.text = task.endTime
            etTitle.setText(task.content)
            // etTitle.isEnabled = false

            when(task.subTaskDtos.size) {
                0 -> {
                    clContent1.visibility = View.GONE
                    clContent2.visibility = View.GONE
                    clContent3.visibility = View.GONE
                }
                1 -> {
                    if (task.subTaskDtos[0].content.isNotEmpty()) {
                        clContent1.visibility = View.VISIBLE
                        etContent1.setText(task.subTaskDtos[0].content)
                        ivRemoveContent1.visibility = View.GONE
                    } else {
                        clContent1.visibility = View.GONE
                    }
                    clContent2.visibility = View.GONE
                    clContent3.visibility = View.GONE
                }
                2 -> {
                    if (task.subTaskDtos[0].content.isNotEmpty()) {
                        clContent1.visibility = View.VISIBLE
                        etContent1.setText(task.subTaskDtos[0].content)
                        ivRemoveContent1.visibility = View.GONE
                    } else {
                        clContent1.visibility = View.GONE
                    }

                    if (task.subTaskDtos[1].content.isNotEmpty()) {
                        clContent2.visibility = View.VISIBLE
                        etContent2.setText(task.subTaskDtos[1].content)
                        ivRemoveContent2.visibility = View.GONE
                    } else {
                        clContent2.visibility = View.GONE
                    }

                    clContent3.visibility = View.GONE
                }
                3 -> {
                    if (task.subTaskDtos[0].content.isNotEmpty()) {
                        clContent1.visibility = View.VISIBLE
                        etContent1.setText(task.subTaskDtos[0].content)
                        ivRemoveContent1.visibility = View.GONE
                    } else {
                        clContent1.visibility = View.GONE
                    }

                    if (task.subTaskDtos[1].content.isNotEmpty()) {
                        clContent2.visibility = View.VISIBLE
                        etContent2.setText(task.subTaskDtos[1].content)
                        ivRemoveContent2.visibility = View.GONE
                    } else {
                        clContent2.visibility = View.GONE
                    }

                    if (task.subTaskDtos[2].content.isNotEmpty()) {
                        clContent3.visibility = View.VISIBLE
                        etContent3.setText(task.subTaskDtos[2].content)
                        ivRemoveContent3.visibility = View.GONE
                    } else {
                        clContent3.visibility = View.GONE
                    }
                }
            }
            ibAddContent.visibility = View.GONE

            btnDelete.setOnClickListener {
                ConfirmDialog(task).show(this@TaskDialog.parentFragmentManager, "ConfirmDialog")
                dismiss()
            }

            btnModify.setOnClickListener {
                modifyTask()
            }

            btnSave.text = "확인"
            btnSave.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun modifyTask() {
        binding.apply {
            // 사이즈 개수 만큼 visible

            clContent1.visibility = View.GONE
            ivRemoveContent1.setOnClickListener {
                etContent1.setText("")
                clContent1.visibility = View.GONE
                ibAddContent.visibility = View.VISIBLE
            }

            clContent2.visibility = View.GONE
            ivRemoveContent2.setOnClickListener {
                etContent2.setText("")
                clContent2.visibility = View.GONE
                ibAddContent.visibility = View.VISIBLE
            }

            clContent3.visibility = View.GONE
            ivRemoveContent3.setOnClickListener {
                etContent3.setText("")
                clContent3.visibility = View.GONE
                ibAddContent.visibility = View.VISIBLE
            }

            ibAddContent.visibility = View.VISIBLE


            ibAddContent.setOnClickListener {
                when {
                    clContent1.visibility == View.GONE -> {
                        clContent1.visibility = View.VISIBLE
                        ivRemoveContent1.visibility = View.VISIBLE
                        if(clContent1.visibility == View.VISIBLE && clContent2.visibility == View.VISIBLE && clContent3.visibility == View.VISIBLE){
                            ibAddContent.visibility = View.GONE
                        }
                    }
                    clContent2.visibility == View.GONE -> {
                        clContent2.visibility = View.VISIBLE
                        ivRemoveContent2.visibility = View.VISIBLE
                        if(clContent1.visibility == View.VISIBLE && clContent2.visibility == View.VISIBLE && clContent3.visibility == View.VISIBLE){
                            ibAddContent.visibility = View.GONE
                        }
                    }
                    clContent3.visibility == View.GONE -> {
                        clContent3.visibility = View.VISIBLE
                        ivRemoveContent3.visibility = View.VISIBLE
                        if(clContent1.visibility == View.VISIBLE && clContent2.visibility == View.VISIBLE && clContent3.visibility == View.VISIBLE){
                            ibAddContent.visibility = View.GONE
                        }
                    }
                }
            }

            btnDelete.text = "취소"
            btnDelete.setOnClickListener {
                dismiss()
            }

            btnModify.visibility = View.GONE

            btnSave.text = "저장"
            btnSave.setOnClickListener {
                // 태스크 수정 함수
            }

        }
    }


}