package com.d108.sduty.ui.main.timer

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.d108.sduty.R
import com.d108.sduty.databinding.FragmentReportBinding
import com.d108.sduty.model.dto.Report
import com.d108.sduty.ui.main.timer.adapter.TaskListAdapter
import com.d108.sduty.ui.main.timer.dialog.TaskDialog
import com.d108.sduty.ui.main.timer.viewmodel.TimerViewModel
import com.d108.sduty.ui.viewmodel.MainViewModel
import com.d108.sduty.utils.convertDpToPx
import com.d108.sduty.utils.convertTimeDateToString
import com.d108.sduty.utils.showToast
import java.util.*

private const val TAG = "ReportFragment"

class ReportFragment : Fragment() {
    private lateinit var binding: FragmentReportBinding

    private val mainViewModel: MainViewModel by activityViewModels()
    private val timerViewModel: TimerViewModel by viewModels({ requireActivity() })

    private lateinit var today: String

    private lateinit var taskListAdapter: TaskListAdapter

    private var colorPalette = listOf<Int>(
        R.color.sduty_light_blue,
        R.color.sduty_main_blue,
        R.color.sduty_light_purple,
        R.color.sduty_main_purple
    )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainViewModel.displayBottomNav(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰모델 초기화
        initViewModel()
        // 화면 초기화
        initView()
        // 해야할 일 갱신
        updateAdapter(timerViewModel.report.value!!)

    }

    private fun initViewModel() {
        timerViewModel.apply {
            report.observe(viewLifecycleOwner) { report ->
                updateAdapter(report)
                updatePlanner(report)
                // 하루 공부한 시간
                binding.tvTotalTime.text = report.totalTime
            }

            isInsertedTask.observe(viewLifecycleOwner) { succeeded ->
                if (succeeded) {
                    requireContext().showToast("등록이 완료되었습니다.")
                    resetLiveData("isInsertedTask")
                }
            }

            isUpdatedTask.observe(viewLifecycleOwner) { succeeded ->
                if (succeeded) {
                    requireContext().showToast("수정이 완료되었습니다.")
                    resetLiveData("isUpdatedTask")
                }
            }

            isDeletedTask.observe(viewLifecycleOwner) { succeeded ->
                if (succeeded) {
                    requireContext().showToast("삭제가 완료되었습니다.")
                    resetLiveData("isDeletedTask")
                }
            }

            isErredConnection.observe(viewLifecycleOwner) { erred ->
                if (erred) {
                    requireContext().showToast("서버와 연결하는데 실패했습니다.")
                    resetLiveData("isErredConnection")
                }
            }
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        // 날짜 선택 후 동작할 리스너
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, day ->
            val selectedDate = "${year}년 ${month + 1}월 ${day}일"
            binding.tvSelectedDate.text = selectedDate
            timerViewModel.selectDate(mainViewModel.user.value!!.seq, selectedDate)

            when (selectedDate != today) {
                true -> { // 오늘
                    binding.apply {
                        btnReturnToday.visibility = View.VISIBLE
                    }
                }
                false -> { // 다른 날
                    binding.apply {
                        btnReturnToday.visibility = View.GONE
                    }
                }
            }
        }
        // 캘린더 다이얼로그 출력
        DatePickerDialog(
            requireActivity(), R.style.MyDatePickerStyle, dateSetListener, cal.get(Calendar.YEAR), cal.get(
                Calendar.MONTH
            ), cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun initView() {
        today = convertTimeDateToString(Date(System.currentTimeMillis()), "yyyy년 M월 d일")

        binding.apply {
            // 날짜 선택
            tvSelectedDate.setOnClickListener {
                showDatePicker()
            }

            commonMore.setOnClickListener {
                TaskDialog().apply {
                    arguments = Bundle().apply {
                        putString("Action", "CustomAdd")
                    }
                    show(this@ReportFragment.requireActivity().supportFragmentManager, "TaskDialog")
                }
            }

            // 오늘 날짜로 돌아가기
            btnReturnToday.setOnClickListener {
                tvSelectedDate.text = today
                btnReturnToday.visibility = View.INVISIBLE
                timerViewModel.selectDate(mainViewModel.user.value!!.seq, today)
            }

            fabTimer.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        // 첫 화면은 오늘 날짜로 설정
        binding.tvSelectedDate.text = today
        timerViewModel.selectDate(mainViewModel.user.value!!.seq, today)
    }

    // 리사이클러뷰 갱신
    private fun updateAdapter(report: Report) {
        taskListAdapter = TaskListAdapter(requireActivity(), report.tasks, colorPalette)
        taskListAdapter.onClickTaskItem = object : TaskListAdapter.OnClickTaskItem {
            override fun onClick(view: View, fragmentActivity: FragmentActivity, position: Int) {

                TaskDialog().apply {
                    arguments = Bundle().apply {
                        putString("Action", "Info")
                        putInt("Position", position)
                    }
                    show(fragmentActivity.supportFragmentManager, "TaskDialog")
                }
            }
        }
        binding.rvReport.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = taskListAdapter
        }
    }

    private fun updatePlanner(report: Report) {

        val tasks = report.tasks
        val table = Array(24) { BooleanArray(6) { false } }
        var num = -1
        var numTable = Array(24) { IntArray(6) { 0 } }

        for (task in tasks) {
            num++
            num %= 4
            val startTime = task.startTime.split(':')
            val startHour = startTime[0].toInt()
            val startMin = startTime[1].toInt() / 10

            val endTime = task.endTime.split(':')
            val endHour = endTime[0].toInt()
            val endMin = endTime[1].toInt() / 10

            for (i in startHour..endHour) {
                if (startHour == endHour) {
                    for (j in startMin..endMin) {
                        table[i][j] = true
                        numTable[i][j] = num
                    }
                } else {
                    when (i) {
                        startHour -> {
                            for (j in startMin..5) {
                                table[i][j] = true
                                numTable[i][j] = num
                            }
                        }
                        endHour -> {
                            if (endMin != 0) {
                                for (j in 0..endMin) {
                                    table[i][j] = true
                                    numTable[i][j] = num
                                }
                            }
                        }
                        else -> {
                            for (j in 0..5) {
                                table[i][j] = true
                                numTable[i][j] = num
                            }
                        }
                    }
                }
            }
        }

        binding.tlPlanner.removeAllViews()

        for (i in 0..23) {
            // 행
            val tableRow = TableRow(requireContext())
            tableRow.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )

            // 시간
            val tvTime = TextView(requireContext())
            val boxSize = convertDpToPx(requireContext(), 22f)
            val marginsSize = convertDpToPx(requireContext(), 0.5f)

            tvTime.layoutParams = TableRow.LayoutParams(boxSize, boxSize).apply {
                setMargins(marginsSize, marginsSize, marginsSize, 0)
            }
            tvTime.setBackgroundColor(Color.WHITE)
            tvTime.text = String.format("%02d",i)
            tvTime.gravity = Gravity.CENTER or Gravity.CENTER_VERTICAL

            tableRow.addView(tvTime)

            // 실제 공부한 시간
            for (j in 0..5) {
                val view = View(requireContext())
                view.layoutParams = TableRow.LayoutParams(boxSize, boxSize).apply {
                    setMargins(marginsSize, marginsSize, marginsSize, marginsSize)
                }
                when (table[i][j]) {
                    true -> {
                        view.setBackgroundResource(colorPalette[numTable[i][j]])
                        view.setOnClickListener {
                            TaskDialog().apply {
                                arguments = Bundle().apply {
                                    putString("Action", "Info")
                                    putInt("Position", numTable[i][j])
                                }
                                show(this@ReportFragment.requireActivity().supportFragmentManager, "TaskDialog")
                            }
                        }
                    }
                    false -> view.setBackgroundResource(R.color.white)
                }
                tableRow.addView(view)
            }

            binding.tlPlanner.addView(tableRow)
        }


    }

}