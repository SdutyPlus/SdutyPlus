package com.d108.sduty.ui.main.study.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import com.d108.sduty.databinding.DialogRadioStudyBinding

private const val TAG = "StudyRadioDialog"
class StudyRadioDialog(context: Context, titles: String, contents: String, btn_a: String, btn_b: String, item: Array<String>) : DialogFragment() {
    private lateinit var binding: DialogRadioStudyBinding

    private val titleText = titles
    private val contentText = contents
    private val buttonAText = btn_a
    private val buttonBText = btn_b
    private val itemList = item


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogRadioStudyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showDialog()
    }

    override fun onResume() {
        super.onResume()

        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes

        // 앱을 실행한 디바이스의 가로, 세로 크기를 가져온다.
        val deviceWidth = com.d108.sduty.utils.getDeviceSize(requireActivity()).x

        // 다이얼로그 크기를 디바이스 가로의 90%로 설정한다.
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    lateinit var onClickListener: OnDialogClickListener
    interface OnDialogClickListener {
        fun onClicked(checkedRadioButtonId: Int)
    }


    fun showDialog() {
        dialog?.show()

        binding.tvTitle.text = titleText
        binding.tvContent.text = contentText
        binding.btnCheck.text = buttonAText
        binding.btnCancel.text = buttonBText

        if(binding.btnCheck.text == "변경"){
            binding.btnCheck.setTextColor(Color.parseColor("#9585EB"))
        }

        if(itemList.isEmpty()){
            binding.tvContent.text = "현재 그룹원이 없습니다."
        } else{
            for(i in 0 until itemList.size){
                val radioBtn = RadioButton(context)
                radioBtn.id = i
                radioBtn.text = itemList[i]
                binding.radioGroup.addView(radioBtn)
            }
        }

        binding.btnCheck.setOnClickListener {
            onClickListener.onClicked(binding.radioGroup.checkedRadioButtonId)
            dialog?.dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }
}