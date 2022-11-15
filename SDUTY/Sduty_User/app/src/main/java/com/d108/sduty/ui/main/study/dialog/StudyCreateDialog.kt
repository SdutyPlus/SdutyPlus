package com.d108.sduty.ui.main.study.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.d108.sduty.R

class StudyCreateDialog(context: Context)
{
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {
        dialog.setContentView(R.layout.dialog_study_create)
        dialog.window?.setGravity(Gravity.TOP)
        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        //dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val normalStudyRegister = dialog.findViewById<ConstraintLayout>(R.id.btn_normalStudy)
        val camStudyRegister = dialog.findViewById<ConstraintLayout>(R.id.btn_camStudy)
        val back = dialog.findViewById<ImageView>(R.id.common_top_back)

        normalStudyRegister.setOnClickListener {
            onClickListener.onClicked(false)
            dialog.dismiss()
        }

        camStudyRegister.setOnClickListener {
            onClickListener.onClicked(true)
            dialog.dismiss()
        }

        back.setOnClickListener {
            dialog.dismiss()
        }


    }

    interface OnDialogClickListener
    {
        fun onClicked(type: Boolean)
    }

}
