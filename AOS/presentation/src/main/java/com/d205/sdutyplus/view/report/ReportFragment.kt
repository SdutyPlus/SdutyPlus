package com.d205.sdutyplus.view.report

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.d205.domain.model.report.Task
import com.d205.sdutyplus.R
import com.d205.sdutyplus.base.BaseFragment
import com.d205.sdutyplus.databinding.Example1CalendarDayBinding
import com.d205.sdutyplus.databinding.FragmentReportBinding
import com.d205.sdutyplus.uitls.displayText
import com.d205.sdutyplus.uitls.setTextColorRes
import com.d205.sdutyplus.view.report.dialog.TaskDialog
import com.kizitonwose.calendar.core.*
import com.kizitonwose.calendar.view.ViewContainer
import com.kizitonwose.calendar.view.WeekCalendarView
import com.kizitonwose.calendar.view.WeekDayBinder
import dagger.hilt.android.AndroidEntryPoint
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@AndroidEntryPoint
class ReportFragment : BaseFragment<FragmentReportBinding>(R.layout.fragment_report),
    TaskAdapterListener {
    private val reportViewModel: ReportViewModel by activityViewModels()
    private val weekCalendarView: WeekCalendarView get() = binding.WeekCalendar
    private val taskAdapter = TaskAdapter(this)
    private val todayDate = LocalDate.now()


    override fun initOnViewCreated() {
        binding.apply {
            reportVM = reportViewModel
            rvReport.adapter = taskAdapter
            tvSelectedDate.text = todayDate.toString()
        }
        initView()
        initViewModelCallback()
        initClickListener()
    }

    private fun initViewModelCallback() {
        reportViewModel.taskCheck.observe(viewLifecycleOwner) {
            Log.d(TAG, "initViewModelCallback11: $it")
            if (it) {
                binding.apply {
                    tvNotice.visibility = View.GONE
                    lottie.visibility = View.GONE
                    scrollTask.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    tvNotice.visibility = View.VISIBLE
                    lottie.visibility = View.VISIBLE
                    scrollTask.visibility = View.GONE
                }
            }
        }

        reportViewModel.updateTaskSuccess.observe(viewLifecycleOwner) {
            if(it) {
                initView()
            }
        }
        
        reportViewModel.deleteTaskSuccess.observe(viewLifecycleOwner) {
            if(it) {
                initView()
            }
        }
    }

    private fun initView() {
        val daysOfWeek = daysOfWeek()

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)

        setupWeekCalendar(startMonth, endMonth, currentMonth, daysOfWeek)

        reportViewModel.apply {
            getReportTotalTime(binding.tvSelectedDate.text.toString())
            getTaskList(binding.tvSelectedDate.text.toString())
        }


    }

    private fun initClickListener() {
        binding.ivCalendarCall.setOnClickListener {
            val dialog = CalendarBottomSheetFragment(binding.tvSelectedDate.text.toString())
            dialog.show(parentFragmentManager, "BottomSheet")
            dialog.setOnClickListener {
                binding.tvSelectedDate.text = it
                initView()
            }
        }
        binding.ivAddTask.setOnClickListener {
            TaskDialog(Task(0, "", "", "")).apply {
                arguments = Bundle().apply {
                    putString("Action", "CustomAdd")
                }
                show(this@ReportFragment.parentFragmentManager, "TaskDialog")
            }
        }
    }

    override fun onTaskClicked(task: Task) {
        TaskDialog(task).apply {
            arguments = Bundle().apply {
                putString("Action", "Info")
            }
            show(this@ReportFragment.parentFragmentManager, "TaskDialog")
        }
    }

    private fun setupWeekCalendar(
        startMonth: YearMonth?,
        endMonth: YearMonth?,
        currentMonth: YearMonth?,
        daysOfWeek: List<DayOfWeek>
    ) {
        class WeekDayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: WeekDay
            val textView = Example1CalendarDayBinding.bind(view).exOneDayText

            init {
                view.setOnClickListener {
                    if(day.position == WeekDayPosition.RangeDate){
                        dateClicked(date = day.date)
                    }
                }
            }
        }

        weekCalendarView.dayBinder = object : WeekDayBinder<WeekDayViewContainer> {
            override fun create(view: View): WeekDayViewContainer = WeekDayViewContainer(view)
            override fun bind(container: WeekDayViewContainer, data: WeekDay) {
                container.day = data
                bindDate(data.date, container.textView, data.position == WeekDayPosition.RangeDate)
            }
        }

        weekCalendarView.weekScrollListener = { updateTitle() }


        startMonth?.let {
            if (endMonth != null) {
                weekCalendarView.setup(
                    it.atStartOfMonth(),
                    endMonth.atEndOfMonth(),
                    daysOfWeek.first(),
                )
            }
        }

        if (currentMonth != null) {
            weekCalendarView.scrollToWeek(LocalDate.parse(binding.tvSelectedDate.text, DateTimeFormatter.ISO_DATE))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateClicked(date: LocalDate) {
        binding.tvSelectedDate.text = date.toString()
        initView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun updateTitle() {
        val week = weekCalendarView.findFirstVisibleWeek() ?: return
        val firstDate = week.days.first().date
        val lastDate = week.days.last().date
        Log.d(TAG, "updateTitle: ${binding.tvSelectedDate.text}")
        if (firstDate.yearMonth == lastDate.yearMonth) {
            binding.exOneYearText.text = firstDate.year.toString()
            binding.exOneMonthText.text = firstDate.month.displayText(short = false) +
                    " " + binding.tvSelectedDate.text.substring(8, 10)
        }
        else {
            binding.exOneMonthText.text =
                firstDate.month.displayText(short = false) +
                        " " + binding.tvSelectedDate.text.substring(8, 10)
            //+ " - " + lastDate.month.displayText(short = false)
            if (firstDate.year == lastDate.year) {
                binding.exOneYearText.text = firstDate.year.toString()
            } else {
                binding.exOneYearText.text = "${firstDate.year} - ${lastDate.year}"
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindDate(date: LocalDate, textView: TextView, isSelectable: Boolean) {
        textView.text = date.dayOfMonth.toString()
        if (isSelectable) {
            when {
                todayDate == date -> {
                    textView.apply {
                        setTextColorRes(R.color.white)
                        setBackgroundResource(R.drawable.bg_calendar_today)
                    }
                }

                binding.tvSelectedDate.text.toString() == date.toString() -> {
                    textView.apply {
                        setTextColorRes(R.color.black)
                        setBackgroundResource(R.drawable.bg_calendar_selected)
                    }
                }

                else -> {
                    textView.apply {
                        setTextColorRes(R.color.black)
                        background = null
                    }
                }
            }
        } else {
            textView.apply {
                setTextColorRes(R.color.sduty_action_off)
                background = null
            }
        }
    }


}