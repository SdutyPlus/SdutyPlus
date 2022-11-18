package com.d205.sdutyplus.view.report.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.d205.sdutyplus.databinding.DialogTimePickerBinding

class CustomTimePickerDialog(context: Context, private val date: String, private val customTimePickerDialogClickListener: CustomTimePickerDialogClickListener): DialogFragment() {
    private lateinit var binding: DialogTimePickerBinding
    private var setHourValue = ""
    private var setMinuteValue = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogTimePickerBinding.inflate(inflater, container, false)
        initDialogWindow()
        return binding.root
    }

    private fun initDialogWindow() {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            requestFeature(Window.FEATURE_NO_TITLE)
        }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initClickListener()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initView() {
        binding.apply {
            if(date != "") {
                tpTime.hour = date.substring(0, 2).toInt()
                tpTime.minute = date.substring(3, 5).toInt()
            }
            tpTime.setIs24HourView(true)
            tpTime.setOnTimeChangedListener(object : TimePicker.OnTimeChangedListener {
                override fun onTimeChanged(p0: TimePicker?, p1: Int, p2: Int) {
                    if(p1 < 10){
                        setHourValue = "0$p1"
                    } else {
                        setHourValue = p1.toString()
                    }
                    if(p2 < 10){
                        setMinuteValue = "0$p2"
                    } else {
                        setMinuteValue = p2.toString()
                    }
                }
            })

        }
    }

    private fun initClickListener() {
        binding.apply {
            btnCheck.setOnClickListener {
                customTimePickerDialogClickListener.onPositiveClick(setHourValue, setMinuteValue)
                dismiss()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }
}