package com.d205.sdutyplus.view.report

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import com.d205.sdutyplus.R
import com.d205.sdutyplus.databinding.FragmentCalendarBottomSheetBinding
import com.d205.sdutyplus.databinding.ResourceCalendarDayBinding
import com.d205.sdutyplus.utils.displayText
import com.d205.sdutyplus.utils.setTextColorRes
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.DayPosition
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.view.CalendarView
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.ViewContainer
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class CalendarBottomSheetFragment(private val selectDate: String, private val dateList: List<String>) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCalendarBottomSheetBinding
    private val monthCalendarView: CalendarView get() = binding.calendarMonth
    private val todayDate = LocalDate.now()
    private lateinit var listener: dayClickListener
    private val reportViewModel: ReportViewModel by activityViewModels()
    private val reportDateList: List<String> = dateList

    interface dayClickListener {
        fun onClick(date: String)
    }

    fun setOnClickListener(listener: (String) -> Unit) {
        this.listener = object : dayClickListener {
            override fun onClick(date: String) {
                listener(date)
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog.setCanceledOnTouchOutside(true)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = BottomSheetDialog(requireContext(), R.style.NewDialog)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupRatio(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

    }

    private fun getBottomSheetDialogDefaultHeight(): Int {
        return getWindowHeight() * 75 / 100
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCalendarBottomSheetBinding.bind(view)

        initViewModelCallback()

        val daysOfWeek = daysOfWeek()

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(100)
        val endMonth = currentMonth.plusMonths(100)
        setupMonthCalendar(startMonth, endMonth, currentMonth, daysOfWeek)
    }

    private fun initViewModelCallback() {
//        reportViewModel.date.observe(viewLifecycleOwner) {
//            dateList = listOf()
//        }
    }

    private fun setupMonthCalendar(
        startMonth: YearMonth,
        endMonth: YearMonth,
        currentMonth: YearMonth,
        daysOfWeek: List<DayOfWeek>,
    ) {
        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val textView = ResourceCalendarDayBinding.bind(view).tvDay

            init {
                view.setOnClickListener {
                    if (day.position == DayPosition.MonthDate) {
                        dateClicked(date = day.date)
                    }
                }
            }
        }
        monthCalendarView.dayBinder = object : MonthDayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)

            @RequiresApi(Build.VERSION_CODES.O)
            override fun bind(container: DayViewContainer, data: CalendarDay) {
                container.day = data
                bindDate(data.date, container.textView, data.position == DayPosition.MonthDate)
            }
        }

        monthCalendarView.apply {
            monthScrollListener = { updateTitle() }
            setup(startMonth, endMonth, daysOfWeek.first())
            scrollToMonth(currentMonth)
        }
    }

    private fun dateClicked(date: LocalDate) {
        listener.onClick(date.toString())
        dismiss()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindDate(date: LocalDate, textView: TextView, isSelectable: Boolean) {
        textView.text = date.dayOfMonth.toString()
        if (isSelectable) {
            for (element in dateList) {
                when (date.toString()) {
                    selectDate -> {
                        textView.apply {
                            setTextColorRes(R.color.white)
                            setBackgroundResource(R.drawable.bg_calendar_selected)
                        }
                    }

                    todayDate.toString() -> {
                        textView.apply {
                            setTextColorRes(R.color.black)
                            setBackgroundResource(R.drawable.bg_calendar_today)
                        }
                    }

                    element -> {
                        textView.apply {
                            setTextColorRes(R.color.black)
                            setBackgroundResource(R.drawable.bg_calendar_study)
                        }
                        break
                    }

                    else -> {
                        textView.apply {
                            setTextColorRes(R.color.black)
                            background = null
                        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun updateTitle() {
        val month = monthCalendarView.findFirstVisibleMonth()?.yearMonth ?: return
        binding.apply {
            tvYear.text = month.year.toString()
            tvMonth.text = month.month.displayText(short = false)
        }
    }

}