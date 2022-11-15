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
import android.widget.TextView
import com.d108.sduty.R
import com.d108.sduty.model.dto.Study
import com.d108.sduty.utils.currentWindowMetricsPointCompat

class StudyDetailDialog(context: Context, studyInfo: Study, masterNickname: String, masterJob: String) {
    private val dialog = Dialog(context)
    private lateinit var onClickListener: OnDialogClickListener
    private val studyInfo = studyInfo
    private val context = context
    private val nickname = masterNickname
    private val job = masterJob

    fun setOnClickListener(listener: OnDialogClickListener)
    {
        onClickListener = listener
    }

    fun showDialog()
    {

        dialog.setContentView(R.layout.dialog_study_detail)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes

        // 앱을 실행한 디바이스의 가로, 세로 크기를 가져온다.
        val deviceWidth = getDeviceSize(context as Activity).x

        // 다이얼로그 크기를 디바이스 가로의 90%로 설정한다.
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams

        dialog.show()

        val studyJoinButton = dialog.findViewById<Button>(R.id.btn_join)
        val cancelButton = dialog.findViewById<Button>(R.id.btn_cancel)

        val studyName = dialog.findViewById<TextView>(R.id.study_name)
        val studyCategory = dialog.findViewById<TextView>(R.id.study_category)
        val studyMaster = dialog.findViewById<TextView>(R.id.tv_study_master)
        val studyJoinPeople = dialog.findViewById<TextView>(R.id.tv_study_join_people)
        val studyLimitPeople = dialog.findViewById<TextView>(R.id.tv_study_limit_people)
        val studyIntroduce = dialog.findViewById<TextView>(R.id.tv_study_introduce)

        if(studyInfo.roomId == null){
            studyCategory.text = "#일반스터디" + "#" + job + "#" + studyInfo.category
        } else{
            studyCategory.text = "#캠스터디" + "#" + job + "#" + studyInfo.category
        }

        studyMaster.text = nickname
        studyName.text = studyInfo.name
        studyJoinPeople.text = studyInfo.joinNumber.toString()
        studyLimitPeople.text = studyInfo.limitNumber.toString()
        studyIntroduce.text = studyInfo.introduce

        studyJoinButton.setOnClickListener {
            onClickListener.onClicked()
            dialog.dismiss()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }



    }

    private fun getDeviceSize(activity: Activity): Point {
        val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        return windowManager.currentWindowMetricsPointCompat()
    }

    interface OnDialogClickListener
    {
        fun onClicked()
    }
}