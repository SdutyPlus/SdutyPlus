package com.d205.sdutyplus.view.report.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.d205.domain.model.report.Task
import com.d205.sdutyplus.databinding.DialogTaskBinding
import com.d205.sdutyplus.view.report.ReportViewModel
import com.d205.sdutyplus.view.report.TAG
import timePickerDialog

class TaskDialog(private val task: Task) : DialogFragment() {
    private lateinit var binding: DialogTaskBinding
    private val reportViewModel: ReportViewModel by activityViewModels()

    private val contentViews: List<ConstraintLayout> by lazy {
        listOf(binding.clContent1, binding.clContent2, binding.clContent3)
    }

    private val contentEditTexts: List<EditText> by lazy {
        listOf(binding.etContent1, binding.etContent2, binding.etContent3)
    }

    private val removeContentBtns: List<ImageView> by lazy {
        listOf(binding.ivRemoveContent1, binding.ivRemoveContent2, binding.ivRemoveContent3)
    }

    private val contentStrings: MutableList<String> by lazy {
        mutableListOf()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogTaskBinding.inflate(inflater, container, false)
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val action = arguments?.getString("Action", "Add")

        when (action) {
            "CustomAdd" -> {
                AddTask()
            }
            "Info" -> {
                InfoTask()
            }
        }
    }

    private fun AddTask() {

    }

    private fun InfoTask() {
        initInfoView()
        initInfoBtn()
    }

    private fun initInfoView() {
        binding.apply {
            etTitle.isEnabled = false

            for (i in 0 until 3) {
                contentViews[i].visibility = View.GONE
                contentEditTexts[i].isEnabled = false
            }

            // Log.d(TAG, "initInfoView: ${task.startTime} , ${task.endTime}")
            tvStartTime.text = task.startTime.substring(11, 19)
            tvEndTime.text = task.endTime.substring(11, 19)
            etTitle.setText(task.title)
            ibAddContent.visibility = View.GONE

            for (i in 0 until task.contents.size) {
                contentViews[i].visibility = View.VISIBLE
                contentEditTexts[i].setText(task.contents[i])
                removeContentBtns[i].visibility = View.GONE
            }
        }
    }

    private fun initInfoBtn() {
        binding.apply {
            btnDelete.setOnClickListener {
                ConfirmDialog(task.seq).apply {
                    arguments = Bundle().apply {
                        putString("Action", "Delete")
                    }
                    show(this@TaskDialog.parentFragmentManager, "ConfirmDialog")
                }


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
            tvStartTime.setOnClickListener {
                startTimePickerDialog()
            }
            tvEndTime.setOnClickListener {
                endTimePickerDialog()
            }

            etTitle.isEnabled = true

            for (i in 0 until 3) {
                removeContentBtns[i].visibility = View.VISIBLE
                contentEditTexts[i].isEnabled = true
            }

            if (task.contents.size < 3) {
                ibAddContent.visibility = View.VISIBLE
            }

            ibAddContent.setOnClickListener {
                val nextContent: View = getNextInVisibleContentView()
                if (nextContent is ConstraintLayout) {
                    nextContent.visibility = View.VISIBLE
                }
            }

            initRemoveContextBtns()

            btnDelete.text = "취소"
            btnDelete.setOnClickListener {
                dismiss()
            }

            btnModify.visibility = View.GONE


            btnSave.text = "저장"
            btnSave.setOnClickListener {
                val content: MutableList<String> = mutableListOf()
                for (i in 0 until 3) {
                    if (contentEditTexts[i].text.toString() != "") {
                        content.add(contentEditTexts[i].text.toString())
                    }
                }
                val updateTask = Task(
                    task.seq,
                    task.startTime.replace(
                        task.startTime.substring(11, 19),
                        tvStartTime.text.toString()
                    ),
                    task.endTime.replace(
                        task.endTime.substring(11, 19),
                        tvEndTime.text.toString()),
                    etTitle.text.toString(),
                    content
                )
                reportViewModel.updateTask(task.seq, updateTask)
                Log.d(TAG, "modifyTaskaa: ${updateTask}")
                dismiss()
            }

        }
    }

    private fun startTimePickerDialog() {
        timePickerDialog(requireContext(),
            binding.tvStartTime.text.toString(),
            parentFragmentManager,
            object : CustomTimePickerDialogClickListener {
                override fun onPositiveClick(hour: String, minute: String) {
                    if (hour != "" && minute != "") {
                        binding.tvStartTime.text = "${hour}:${minute}:00"
                    }
                }
            })
    }

    private fun endTimePickerDialog() {
        timePickerDialog(requireContext(),
            binding.tvEndTime.text.toString(),
            parentFragmentManager,
            object : CustomTimePickerDialogClickListener {
                @SuppressLint("SetTextI18n")
                override fun onPositiveClick(hour: String, minute: String) {
                    if (hour != "" && minute != "") {
                        if (hour.toInt() >= binding.tvStartTime.text.substring(0, 2)
                                .toInt() &&
                            (minute.toInt() > binding.tvStartTime.text.substring(3, 5)
                                .toInt() ||
                                    hour.toInt() > binding.tvStartTime.text.substring(0, 2)
                                .toInt())
                        ) {
                            binding.tvEndTime.text = "${hour}:${minute}:00"
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "시작 시간보다 이른 시간으로 설정할 수 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
    }

    private fun initRemoveContextBtns() {
        for ((index, removeBtn) in removeContentBtns.withIndex()) {
            removeBtn.setOnClickListener {

                // visible 인 content의 내용들을 저장
                for ((index, contentView) in contentViews.withIndex()) {
                    if (contentView.visibility == View.VISIBLE) {
                        contentStrings.add(contentEditTexts[index].text.toString())
                    }
                }

                // remove 한 index의 내용을 삭제
                contentStrings.removeAt(index)

                // 가장 뒤에서 visible인 contentView를 Gone 처리
                for ((index, contentView) in contentViews.reversed().withIndex()) {
                    if (contentView.visibility == View.VISIBLE) {
                        contentView.visibility = View.GONE
                        contentEditTexts[2 - index].setText("")
                        break
                    }
                }

                // Visible인 Content들에 SetText
                var count = 0
                for ((index, contentView) in contentViews.withIndex()) {
                    if (contentView.visibility == View.VISIBLE) {
                        contentEditTexts[index].setText(contentStrings[index])
                        count++
                    }
                }

                //Strings 배열 초기화
                contentStrings.clear()
                if (count != 3) {
                    binding.ibAddContent.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun getNextInVisibleContentView(): View {
        for ((index, contentView) in contentViews.withIndex()) {
            if (contentView.visibility == View.GONE) {
                if (index == contentViews.size - 1) {
                    binding.ibAddContent.visibility = View.GONE
                }
                return contentView
            }
        }
        return View(requireContext())
    }


}