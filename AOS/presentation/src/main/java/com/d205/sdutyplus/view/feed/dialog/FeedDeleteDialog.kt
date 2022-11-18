package com.d205.sdutyplus.view.feed.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.d205.sdutyplus.R
import com.d205.sdutyplus.databinding.DialogFeedDeleteBinding

class FeedDeleteDialog(context: Context, private val listener: FeedDeleteDialogListener) : Dialog(context){
    private lateinit var binding: DialogFeedDeleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_feed_delete, null, false)
        setContentView(binding.root)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {
            btnDelete.setOnClickListener {
                listener.onOkButtonClicked()
                dismiss()
            }
            btnCancel.setOnClickListener {
                dismiss()
            }
        }
    }
}