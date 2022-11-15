package com.d205.sdutyplus.view.report.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.d205.domain.model.timer.CurrentTaskDto2
import com.d205.sdutyplus.databinding.DialogCustomTaskRegistBinding
import com.d205.sdutyplus.uitls.getDeviceSize
import com.d205.sdutyplus.view.report.ReportViewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class CustomTaskRegistDialog : DialogFragment() {
    private lateinit var binding: DialogCustomTaskRegistBinding
    private val reportViewModel: ReportViewModel by activityViewModels()

    var calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA)
    var mHour: Int = calendar.get(Calendar.HOUR_OF_DAY)
    var mMinute: Int = calendar.get(Calendar.MINUTE)

    @RequiresApi(Build.VERSION_CODES.O)
    private val todayDate = LocalDate.now()
    private val todayTime = System.currentTimeMillis()

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogCustomTaskRegistBinding.inflate(inflater, container, false)

        initDialogWindow()

        return binding.root
    }

    private fun initDialogWindow() {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClickListener()
        initViewModelCallBack()
    }

    private fun initViewModelCallBack() {
        reportViewModel.addTaskCallBack.observe(viewLifecycleOwner) { code ->
            if (code == 200) {
                Toast.makeText(requireContext(), "기록이 저장되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initView() {
        val dateFormat = SimpleDateFormat("hh:mm")
        binding.apply {
            tvToday.text = todayDate.toString()
            tvStartTime.text = dateFormat.format(todayTime).toString()
            tvEndTime.text = dateFormat.format(todayTime).toString()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun initClickListener() {
        binding.apply {
            ivStartTime.setOnClickListener {
                startTimePickerDialog()
            }

            ivEndTime.setOnClickListener {
                endTimePickerDialog()
            }

            btnSave.setOnClickListener {
                val title = etTitle.text.toString()

                val content1 = etContent1.text.toString()
                val content2 = etContent2.text.toString()
                val content3 = etContent3.text.toString()
                val contents: List<String> = listOf(content1, content2, content3)

                var realContents: MutableList<String> = mutableListOf()
                for(content in contents) {
                    if (content != "") {
                        realContents.add(content)
                    }
                }

                if (title.isNotEmpty()) {
                    reportViewModel.addTask(
                        CurrentTaskDto2(
                            0,
                            "${todayDate} ${tvStartTime.text}:00",
                            "${todayDate} ${tvEndTime.text}:00",
                            title,
                            realContents
                        )
                    )
                } else {
                    Toast.makeText(requireContext(), "제목을 입력하세요!", Toast.LENGTH_SHORT).show()
                }
            }

            btnDelete.setOnClickListener {
                dismiss()
            }

            ivAddContent.setOnClickListener {
                val nextContent: View = getNextInVisibleContentView()
                if (nextContent is ConstraintLayout) {
                    nextContent.visibility = View.VISIBLE
                }
            }

            initRemoveContextBtns()
        }
    }

    private fun startTimePickerDialog() {
        val timePickerDialog = CustomTimePickerDialog(
            requireContext(),
            binding.tvStartTime.text.toString(),
            object : CustomTimePickerDialogClickListener {
                override fun onPositiveClick(hour: String, minute: String) {
                    if (hour != "" && minute != "") {
                        if (hour.toInt() <= binding.tvEndTime.text.substring(0, 2).toInt() &&
                            (minute.toInt() < binding.tvEndTime.text.substring(3, 5).toInt() ||
                                    hour.toInt() < binding.tvEndTime.text.substring(0, 2).toInt())
                        ) {
                            binding.tvStartTime.setText("${hour}:${minute}")
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "끝나는 시간보다 늦은 시간으로 설정할 수 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
        timePickerDialog.show(parentFragmentManager, "TimePicker")
    }

    private fun endTimePickerDialog() {
        val timePickerDialog = CustomTimePickerDialog(
            requireContext(),
            binding.tvEndTime.text.toString(),
            object : CustomTimePickerDialogClickListener {
                override fun onPositiveClick(hour: String, minute: String) {
                    if (hour != "" && minute != "") {
                        if (hour.toInt() >= binding.tvStartTime.text.substring(0, 2).toInt() &&
                            (minute.toInt() > binding.tvStartTime.text.substring(3, 5).toInt() ||
                                    hour.toInt() > binding.tvStartTime.text.substring(0, 2).toInt())
                        ) {
                            binding.tvEndTime.setText("${hour}:${minute}")
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
        timePickerDialog.show(parentFragmentManager, "TimePicker")
    }

    private fun getNextInVisibleContentView(): View {
        for ((index, contentView) in contentViews.withIndex()) {
            if (contentView.visibility == View.GONE) {
                if (index == contentViews.size - 1) {
                    binding.ivAddContent.visibility = View.GONE
                }
                return contentView
            }
        }
        return View(requireContext())
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
                    binding.ivAddContent.visibility = View.VISIBLE
                }
            }
        }
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