package com.d205.sdutyplus.view.feed.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.d205.sdutyplus.R
import com.d205.sdutyplus.databinding.DialogFeedReportBinding

class FeedReportDialog (context: Context, private val listener: FeedReportDialogListener) : Dialog(context){
    private lateinit var binding: DialogFeedReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_feed_report, null, false)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
            btnReport.setOnClickListener {
                listener.onReportButtonClicked()
                dismiss()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }
}