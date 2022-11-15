package com.d108.sduty.ui.main.study.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.d108.sduty.R
import com.d108.sduty.utils.currentWindowMetricsPointCompat

class StudyPasswordDialog(context: Context) {
    private val dialog = Dialog(context)
    private lateinit var onClickListener: StudyPasswordDialog.OnDialogClickListener
    private val context = context

    fun setOnClickListener(listener: StudyPasswordDialog.OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog() {
        dialog.setContentView(R.layout.dialog_study_password)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes

        // 앱을 실행한 디바이스의 가로, 세로 크기를 가져온다.
        val deviceWidth = getDeviceSize(context as Activity).x

        // 다이얼로그 크기를 디바이스 가로의 90%로 설정한다.
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams

        dialog.show()

        val etPassword = dialog.findViewById<EditText>(R.id.et_password)
        val check = dialog.findViewById<Button>(R.id.btn_check)
        val cancel = dialog.findViewById<Button>(R.id.btn_cancel)

        check.setOnClickListener {
            onClickListener.onClicked(etPassword)
            dialog.dismiss()
        }

        cancel.setOnClickListener {
            dialog.dismiss()
        }



    }

    private fun getDeviceSize(activity: Activity): Point {
        val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return windowManager.currentWindowMetricsPointCompat()
    }

    interface OnDialogClickListener
    {
        fun onClicked(etPassword: EditText)
    }
}