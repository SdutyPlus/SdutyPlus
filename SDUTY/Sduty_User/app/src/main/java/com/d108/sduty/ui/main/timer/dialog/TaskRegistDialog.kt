package com.d108.sduty.ui.main.timer.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.d108.sduty.databinding.DialogTaskRegistBinding
import com.d108.sduty.model.dto.Report
import com.d108.sduty.model.dto.Task
import com.d108.sduty.ui.main.timer.viewmodel.TimerViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.convertTimeDateToString
import com.d108.sduty.utils.getDeviceSize
import java.util.*

private const val TAG = "TaskDialog"

class TaskRegistDialog : DialogFragment() {
    private lateinit var binding: DialogTaskRegistBinding
    private val timerViewModel: TimerViewModel by viewModels({ requireActivity() })
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogTaskRegistBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 여백 터치 시 다이얼로그 종료 방지
        isCancelable = false

        initView()
    }

    private fun initView() {
        binding.apply {
            val time = timerViewModel.timer.value!!
            val hour = time / 60 / 60
            val min = (time / 60) % 60
            val sec = time % 60
            tvTimer.text = String.format("%02d:%02d:%02d", hour, min, sec)

            clContent1.visibility = View.GONE
            ivRemoveContent1.setOnClickListener {
                // content3의 내용이 비어있지 않으면 내용을 가져오고 content3를 없앤다.
                when {
                    etContent3.text.toString().isNotEmpty() -> {
                        etContent1.setText(etContent2.text.toString())
                        etContent2.setText(etContent3.text.toString())
                        etContent3.setText("")
                        clContent3.visibility = View.GONE
                        ibAddContent.visibility = View.VISIBLE
                    }
                    etContent2.text.toString().isNotEmpty() -> {
                        etContent1.setText(etContent2.text.toString())
                        etContent2.setText("")
                        clContent2.visibility = View.GONE
                        ibAddContent.visibility = View.VISIBLE
                    }
                    else -> {
                        etContent1.setText("")
                        clContent1.visibility = View.GONE
                        ibAddContent.visibility = View.VISIBLE
                    }
                }
            }

            clContent2.visibility = View.GONE
            ivRemoveContent2.setOnClickListener {
                // content3의 내용이 비어있지 않으면 내용을 가져오고 content3를 없앤다.
                if (etContent3.text.toString().isNotEmpty()) {
                    etContent2.setText(etContent3.text.toString())
                    etContent3.setText("")
                    clContent3.visibility = View.GONE
                    ibAddContent.visibility = View.VISIBLE
                } else {
                    etContent2.setText("")
                    clContent2.visibility = View.GONE
                    ibAddContent.visibility = View.VISIBLE
                }
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
                        if (clContent1.visibility == View.VISIBLE && clContent2.visibility == View.VISIBLE && clContent3.visibility == View.VISIBLE) {
                            ibAddContent.visibility = View.GONE
                        }
                    }
                    clContent2.visibility == View.GONE -> {
                        clContent2.visibility = View.VISIBLE
                        if (clContent1.visibility == View.VISIBLE && clContent2.visibility == View.VISIBLE && clContent3.visibility == View.VISIBLE) {
                            ibAddContent.visibility = View.GONE
                        }
                    }
                    clContent3.visibility == View.GONE -> {
                        clContent3.visibility = View.VISIBLE
                        if (clContent1.visibility == View.VISIBLE && clContent2.visibility == View.VISIBLE && clContent3.visibility == View.VISIBLE) {
                            ibAddContent.visibility = View.GONE
                        }
                    }
                }
            }

            btnSave.setOnClickListener {
                // 등록 수행
                val startTime = timerViewModel.startTime
                val endTime = timerViewModel.endTime
                val title = etTitle.text.toString()
                val content1 = etContent1.text.toString()
                val content2 = etContent2.text.toString()
                val content3 = etContent3.text.toString()

                val today = convertTimeDateToString(Date(System.currentTimeMillis()), "yyyy-MM-dd")

                val newTask =
                    Task(-1, -1, endTime, startTime, 0, title, content1, content2, content3)

                val report = Report(-1, mainViewModel.user.value!!.seq, today, "", listOf(newTask))

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
                            this@TaskRegistDialog.requireActivity().supportFragmentManager,
                            "ConfirmDialog"
                        )
                    }
                }
            }

            btnDelete.setOnClickListener {
                ConfirmDialog().apply {
                    // 삭제 경고
                    arguments = Bundle().apply {
                        putString("Action", "RemoveTimer")
                    }
                    show(
                        this@TaskRegistDialog.requireActivity().supportFragmentManager,
                        "ConfirmDialog"
                    )
                }
            }
        }

        timerViewModel.resetDelayTimer()
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes

        // 앱을 실행한 디바이스의 가로, 세로 크기를 가져온다.
        val deviceWidth = getDeviceSize(requireActivity()).x

        // 다이얼로그 크기를 디바이스 가로의 90%로 설정한다.
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}