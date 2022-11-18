package com.d108.sduty.ui.main.timer.dialog

import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.d108.sduty.databinding.DialogTaskBinding
import com.d108.sduty.model.dto.Report
import com.d108.sduty.model.dto.Task
import com.d108.sduty.ui.main.timer.viewmodel.TimerViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.convertTimeDateToString
import com.d108.sduty.utils.convertTimeStringToDate
import com.d108.sduty.utils.getDeviceSize
import java.util.*

private const val TAG = "TaskDialog"

class TaskDialog : DialogFragment() {
    private lateinit var binding: DialogTaskBinding
    private val timerViewModel: TimerViewModel by viewModels({ requireActivity() })
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTaskBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val action = arguments?.getString("Action", "Add")

        when (action) {
            // Task 사용자 설정 추가
            "CustomAdd" -> {
                customAddTask()
            }
            // Task 조회
            "Info" -> {
                infoTask()
            }
        }
    }

    private fun customAddTask() {
        binding.apply {
            tvStartTime.text = "시작 시간"
            tvStartTime.setOnClickListener {
                val startTimeListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                        // 시작 시간이 종료시간보다 빨라야한다.
                        if (tvEndTime.text != "종료 시간" && tvEndTime.text.toString().isNotEmpty()) {
                            val startTime = convertTimeStringToDate(
                                String.format("%02d:%02d:00", hour, min),
                                "HH:mm:ss"
                            ).time

                            val endTime =
                                convertTimeStringToDate(
                                    tvEndTime.text.toString(),
                                    "HH:mm:ss"
                                ).time

                            if (endTime > startTime) {
                                tvStartTime.text = String.format("%02d:%02d:00", hour, min)
                            } else {
                                ConfirmDialog().apply {
                                    // 경고창에 출력할 메시지를 담아 보낸다.
                                    arguments = Bundle().apply {
                                        putString("Action", "Error")
                                        putString("Message", "시작 시간은 종료시간보다 빨라야합니다!")
                                    }
                                    show(
                                        this@TaskDialog.requireActivity().supportFragmentManager,
                                        "ConfirmDialog"
                                    )
                                    tvStartTime.text = "시작 시간"
                                    tvEndTime.text = "종료 시간"
                                }
                            }
                        } else {
                            tvStartTime.text = String.format("%02d:%02d:00", hour, min)
                        }

                    }
                TimePickerDialog(requireActivity(), startTimeListener, 0, 0, true).show()
            }

            tvEndTime.text = "종료 시간"
            tvEndTime.setOnClickListener {
                val endTimeListener =
                    TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                        // 시작 시간이 종료시간보다 빨라야한다.
                        if (tvStartTime.text != "시작 시간" && tvStartTime.text.toString().isNotEmpty()) {
                            val startTime =
                                convertTimeStringToDate(
                                    tvStartTime.text.toString(),
                                    "HH:mm:ss"
                                ).time
                            val endTime = convertTimeStringToDate(
                                String.format("%02d:%02d:00", hour, min),
                                "HH:mm:ss"
                            ).time

                            if (endTime > startTime) {
                                tvEndTime.text = String.format("%02d:%02d:00", hour, min)
                            } else {
                                ConfirmDialog().apply {
                                    // 경고창에 출력할 메시지를 담아 보낸다.
                                    arguments = Bundle().apply {
                                        putString("Action", "Error")
                                        putString("Message", "시작 시간은 종료시간보다 빨라야합니다!")
                                    }
                                    show(
                                        this@TaskDialog.requireActivity().supportFragmentManager,
                                        "ConfirmDialog"
                                    )
                                    tvStartTime.text = "시작 시간"
                                    tvEndTime.text = "종료 시간"
                                }
                            }
                        } else {
                            tvStartTime.text = String.format("%02d:%02d:00", hour, min)
                        }

                    }
                TimePickerDialog(requireActivity(), endTimeListener, 0, 0, true).show()
            }

            clContent1.visibility = View.GONE
            ivRemoveContent1.setOnClickListener {
                etContent1.setText("")
                clContent1.visibility = View.GONE
            }

            clContent2.visibility = View.GONE
            ivRemoveContent2.setOnClickListener {
                etContent2.setText("")
                clContent2.visibility = View.GONE
            }

            clContent3.visibility = View.GONE
            ivRemoveContent3.setOnClickListener {
                etContent3.setText("")
                clContent3.visibility = View.GONE
                ibAddContent.visibility = View.VISIBLE
            }

            ibAddContent.setOnClickListener {
                when {
                    clContent1.visibility == View.GONE -> {
                        clContent1.visibility = View.VISIBLE
                    }
                    clContent2.visibility == View.GONE -> {
                        clContent2.visibility = View.VISIBLE
                    }
                    clContent3.visibility == View.GONE -> {
                        clContent3.visibility = View.VISIBLE
                        ibAddContent.visibility = View.GONE
                    }
                }
            }

            btnSave.setOnClickListener {
                // 등록 수행
                val startTime = tvStartTime.text.toString()
                val endTime = tvEndTime.text.toString()
                val title = etTitle.text.toString()
                val content1 = etContent1.text.toString()
                val content2 = etContent2.text.toString()
                val content3 = etContent3.text.toString()

                val today = convertTimeDateToString(Date(System.currentTimeMillis()), "yyyy-MM-dd")

                val newTask =
                    Task(-1, -1, endTime, startTime, 0, title, content1, content2, content3)

                val report = Report(-1, mainViewModel.user.value!! .seq, today, "", listOf(newTask))

                // title 은 필수로 입력해야한다.
                if (title.isNotEmpty()) {
                    timerViewModel.saveTask(report)
                    timerViewModel.getTodayReport(mainViewModel.user.value!!.seq)
                    dismiss()
                } else {
                    ConfirmDialog().apply {
                        // 경고창에 출력할 메시지를 담아 보낸다.
                        arguments = Bundle().apply {
                            putString("Action", "Error")
                            putString("Message", "제목을 입력해주세요!!")
                        }
                        show(
                            this@TaskDialog.requireActivity().supportFragmentManager,
                            "ConfirmDialog"
                        )
                    }
                }
            }

            btnDelete.text = "취소"
            btnDelete.setOnClickListener {
                dismiss()
            }
            btnModify.visibility = View.GONE
        }
    }

    private fun infoTask() {
        val position = arguments?.getInt("Position", 0)
        val task = timerViewModel.report.value!!.tasks[position!!]
        binding.apply {
//            val hour = task.durationTime / 60 / 60
//            val min = (task.durationTime / 60) % 60
//            val sec = task.durationTime % 60
//
//            tvTimer.text = String.format("%02d:%02d:%02d", hour, min, sec)

            tvStartTime.text = task.startTime

            tvEndTime.text = task.endTime

            etTitle.setText(task.title)
            etTitle.isEnabled = false

            if (task.content1.isNotEmpty()) {
                clContent1.visibility = View.VISIBLE
                etContent1.setText(task.content1)
                ivRemoveContent1.visibility = View.GONE
            } else {
                clContent1.visibility = View.GONE
            }

            if (task.content2.isNotEmpty()) {
                clContent2.visibility = View.VISIBLE
                etContent2.setText(task.content1)
                ivRemoveContent2.visibility = View.GONE
            } else {
                clContent2.visibility = View.GONE
            }

            if (task.content3.isNotEmpty()) {
                clContent3.visibility = View.VISIBLE
                etContent3.setText(task.content1)
                ivRemoveContent3.visibility = View.GONE
            } else {
                clContent3.visibility = View.GONE
            }

            ibAddContent.visibility = View.GONE

            btnDelete.setOnClickListener {
                ConfirmDialog().apply {
                    arguments = Bundle().apply {
                        putString("Action", "RemoveTask")
                        putInt("Position", position)
                    }
                    show(this@TaskDialog.requireActivity().supportFragmentManager, "ConfirmDialog")
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
        val position = arguments?.getInt("Position", 0)
        val task = timerViewModel.report.value!!.tasks[position!!]
        binding.apply {
//            val hour = task.durationTime / 60 / 60
//            val min = (task.durationTime / 60) % 60
//            val sec = task.durationTime % 60
//
//            tvTimer.text = String.format("%02d:%02d:%02d", hour, min, sec)

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
                        if(clContent1.visibility == View.VISIBLE && clContent2.visibility == View.VISIBLE && clContent3.visibility == View.VISIBLE){
                            ibAddContent.visibility = View.GONE
                        }
                    }
                    clContent2.visibility == View.GONE -> {
                        clContent2.visibility = View.VISIBLE
                        if(clContent1.visibility == View.VISIBLE && clContent2.visibility == View.VISIBLE && clContent3.visibility == View.VISIBLE){
                            ibAddContent.visibility = View.GONE
                        }
                    }
                    clContent3.visibility == View.GONE -> {
                        clContent3.visibility = View.VISIBLE
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
                var task = timerViewModel.report.value!!.tasks[position!!]

                task.content1 = etContent1.text.toString()
                task.content2 = etContent2.text.toString()
                task.content3 = etContent3.text.toString()

                timerViewModel.modifyTask(mainViewModel.user.value!!.seq, task)
                dismiss()
            }
        }

//        val position = arguments?.getInt("Position", 0)
//        binding.apply {
//            if (etContent2.visibility == View.GONE && etContent3.visibility == View.GONE) {
//                ivAddContent.visibility = View.VISIBLE
//            } else if (etContent2.visibility == View.VISIBLE && etContent3.visibility == View.GONE) {
//                ivAddContent.visibility = View.VISIBLE
//                ivRemoveContent2.visibility = View.VISIBLE
//            } else {
//                ivAddContent.visibility = View.INVISIBLE
//                ivRemoveContent2.visibility = View.INVISIBLE
//                ivRemoveContent3.visibility = View.VISIBLE
//            }
//
//            // 내용 2, 3 추가 버튼
//            ivAddContent.setOnClickListener {
//                if (etContent2.visibility == View.GONE && etContent3.visibility == View.GONE) {
//                    etContent2.visibility = View.VISIBLE
//                    ivRemoveContent2.visibility = View.VISIBLE
//                } else if (etContent2.visibility == View.VISIBLE && etContent3.visibility == View.GONE) {
//                    ivAddContent.visibility = View.INVISIBLE
//                    ivRemoveContent2.visibility = View.INVISIBLE
//                    etContent3.visibility = View.VISIBLE
//                    ivRemoveContent3.visibility = View.VISIBLE
//                }
//            }
//
//            // 내용 2
//            ivRemoveContent2.setOnClickListener {
//                etContent2.setText("")
//                etContent2.visibility = View.GONE
//                ivRemoveContent2.visibility = View.GONE
//            }
//
//            // 내용 3
//            ivRemoveContent3.setOnClickListener {
//                ivAddContent.visibility = View.VISIBLE
//                ivRemoveContent2.visibility = View.VISIBLE
//                etContent3.setText("")
//                etContent3.visibility = View.GONE
//                ivRemoveContent3.visibility = View.GONE
//            }
//
//            etContent1.isEnabled = true
//            etContent2.isEnabled = true
//            etContent3.isEnabled = true
//
//            btnDelete.text = "취소"
//            btnDelete.setOnClickListener {
//                dismiss()
//            }
//
//            btnModify.visibility = View.GONE
//
//            btnSave.text = "저장"
//            btnSave.setOnClickListener {
//                var task = timerViewModel.report.value!!.tasks[position!!]
//
//                task.content1 = etContent1.text.toString()
//                task.content2 = etContent2.text.toString()
//                task.content3 = etContent3.text.toString()
//
//                timerViewModel.modifyTask(mainViewModel.user.value!!.seq, task)
//                dismiss()
//            }
//        }
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes

        // 앱을 실행한 디바이스의 가로, 세로 크기를 가져온다.
        val deviceWidth = getDeviceSize(requireActivity()).x

        // 다이얼로그 크기를 디바이스 가로의 90%로 설정한다.
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}